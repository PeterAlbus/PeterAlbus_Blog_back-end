package com.peteralbus.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.config.PermissionConfig;
import com.peteralbus.domain.Blog;
import com.peteralbus.domain.Result;
import com.peteralbus.service.BlogService;
import com.peteralbus.util.RedisUtil;
import com.peteralbus.util.ResultUtil;
import com.peteralbus.util.TypeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The type Blog controller.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
@RequestMapping("/blog")
public class BlogController {
    /**
     * The Blog service.
     */
    private final BlogService blogService;
    /**
     * The Redis util.
     */
    private final RedisUtil redisUtil;

    /**
     * Query all list.
     *
     * @return the list
     */
    @GetMapping("/queryAll")
    public Result<List<Blog>> queryAll() {
        return ResultUtil.success(blogService.queryAll());
    }

    /**
     * Query by id result.
     *
     * @param id the id
     * @return the result
     */
    @GetMapping("/queryById")
    public Result<Blog> queryById(Long id) {
        return ResultUtil.success(blogService.queryById(id));
    }


    /**
     * Add blog.
     *
     * @param blog the blog
     * @return the result
     */
    @PostMapping("/add")
    public Result<?> add(Blog blog) {
        if(!StpUtil.hasPermission(PermissionConfig.WRITE_ARTICLE)) {
            return ResultUtil.error(403, "没有发布文章的权限！");
        }
        int status;
        status=blogService.add(blog);
        if(status>0)
        {
            return ResultUtil.success(blog,"发布成功");
        }
        return ResultUtil.error(500,"发布失败，未知原因，请联系管理员");
    }

    /**
     * Update blog.
     *
     * @param blog the blog
     * @return the result
     */
    @PostMapping("/update")
    public Result<?> update(Blog blog) {
        if(!StpUtil.hasPermission(PermissionConfig.MODIFY_ARTICLE)) {
            return ResultUtil.error(403, "没有修改文章的权限！");
        }
        int status;
        status=blogService.update(blog);
        if(status>0)
        {
            return ResultUtil.success(blog,"修改成功");
        }
        return ResultUtil.error(500,"修改失败，未知原因，请联系管理员");
    }

    /**
     * Visit blog.
     *
     * @param blogId    the blog id
     * @param ipAddress the ip address
     * @return the result
     */
    @GetMapping("/visitBlog")
    public Result<?> visitBlog(Long blogId, String ipAddress) {
        String key="blogVisitRecord:"+blogId.toString()+"-"+ipAddress;
        if(redisUtil.exists(key)) {
            return ResultUtil.success(null,"8小时内不可重复增加浏览量");
        }
        Blog blog=blogService.queryById(blogId);
        blog.setBlogViews(blog.getBlogViews()+1);
        blogService.update(blog);
        redisUtil.set(key, "visited", 8L, TimeUnit.HOURS);
        return ResultUtil.success("success","访问成功");
    }

    /**
     * Upload Cover.
     *
     * @param file the file
     * @return the result
     */
    @PostMapping("/upload")
    public Result<?> uploadCover(@RequestParam("file") MultipartFile file) {
        String uploadPath="/home/PeterAlbus/assets/blog/imgs/cover/";
        String fileName = file.getOriginalFilename();
        String type= TypeUtil.getType(fileName);
        if(TypeUtil.isImg(type))
        {
            String newName= UUID.randomUUID().toString().replace("-", "").toLowerCase()+type;
            File dest = new File(uploadPath + newName);
            try {
                file.transferTo(dest);
                return ResultUtil.success("https://file.peteralbus.com/assets/blog/imgs/cover/"+newName,"上传成功");
            } catch (IOException e) {
                return ResultUtil.error(500,"上传错误:"+e.getMessage());
            }
        }
        return ResultUtil.error(400,"文件类型为"+type+",不符合标准");
    }
}
