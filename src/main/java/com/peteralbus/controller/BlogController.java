package com.peteralbus.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.domain.Blog;
import com.peteralbus.service.BlogService;
import com.peteralbus.util.RedisUtils;
import com.peteralbus.util.TypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * The type Blog controller.
 *
 * @author PeterAlbus  Created on 2021/7/20.
 */
@RestController
@CrossOrigin
public class BlogController
{
    /**
     * The Blog service.
     */
    BlogService blogService;
    /**
     * The Redis utils.
     */
    RedisUtils redisUtils;

    /**
     * Sets redis utils.
     *
     * @param redisUtils the redis utils
     */
    @Autowired
    public void setRedisUtils(RedisUtils redisUtils)
    {
        this.redisUtils = redisUtils;
    }

    /**
     * Sets blog service.
     *
     * @param blogService the blog service
     */
    @Autowired
    public void setBlogService(BlogService blogService)
    {
        this.blogService = blogService;
    }

    /**
     * Query all list.
     *
     * @return the list
     */
    @GetMapping("/queryAll")
    public List<Blog> queryAll()
    {
        return blogService.queryAll();
    }

    /**
     * Query by id blog.
     *
     * @param id the id
     * @return the blog
     */
    @GetMapping("/queryById")
    public Blog queryById(Long id)
    {
        return blogService.queryById(id);
    }

    /**
     * Add string.
     *
     * @param blog the blog
     * @return the string
     */
    @PostMapping("/add")
    public String add(Blog blog)
    {
        final String writeArticle="write-article";
        if(!StpUtil.hasPermission(writeArticle))
        {
            return "noPermission";
        }
        int status;
        status=blogService.add(blog);
        if(status>0)
        {
            return blog.toString();
        }
        else
        {
            return "fail";
        }
    }

    /**
     * Update string.
     *
     * @param blog the blog
     * @return the string
     */
    @PostMapping("/update")
    public String update(Blog blog)
    {
        final String modifyArticle="modify-article";
        if(!StpUtil.hasPermission(modifyArticle))
        {
            return "noPermission";
        }
        int status;
        status=blogService.update(blog);
        if(status>0)
        {
            return blog.toString();
        }
        else
        {
            return "fail";
        }
    }

    /**
     * Visit blog string.
     *
     * @param blogId    the blog id
     * @param ipAddress the ip address
     * @return the string
     */
    @GetMapping("/visitBlog")
    public String visitBlog(Long blogId,String ipAddress)
    {
        String key="blogVisitRecord:"+blogId.toString()+"-"+ipAddress;
        if(redisUtils.exists(key))
        {
            return "repeatVisit";
        }
        Blog blog=blogService.queryById(blogId);
        blog.setBlogViews(blog.getBlogViews()+1);
        blogService.update(blog);
        redisUtils.set(key, "visited", 8L,TimeUnit.HOURS);
        return "success";
    }

    /**
     * Upload string.
     *
     * @param file the file
     * @return the string
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file)
    {
        String uploadPath="/home/PeterAlbus/assets/blog/imgs/cover/";
        String fileName = file.getOriginalFilename();
        String type="unknown";
        if(fileName!=null)
        {
            type=fileName.substring(fileName.lastIndexOf('.'));
        }
        if(TypeUtil.isImg(type))
        {
            String newName= UUID.randomUUID().toString().replace("-", "").toLowerCase()+type;
            File dest = new File(uploadPath + newName);
            try {
                file.transferTo(dest);
                return "https://file.peteralbus.com/assets/blog/imgs/cover/"+newName;
            } catch (IOException e) {
                return "上传错误:"+e.getMessage();
            }
        }
        return "typeError";
    }
}
