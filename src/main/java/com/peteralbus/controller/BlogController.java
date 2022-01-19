package com.peteralbus.controller;

import com.peteralbus.domain.Blog;
import com.peteralbus.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 * The type Blog controller.
 * @author PeterAlbus
 * Created on 2021/7/20.
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
    public Blog queryById(Integer id)
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
        int status=-1;
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
        int status=-1;
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
     * Upload string.
     *
     * @param file the file
     * @return the string
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file)
    {
        System.out.println("fileUpload");
        String uploadPath="/home/PeterAlbus/assets/blog/imgs/cover/";
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        String newName= UUID.randomUUID().toString().replace("-", "").toLowerCase();
        File dest = new File(uploadPath + newName);
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            System.out.println("上传成功，当前上传的文件保存在"+"https://www.peteralbus.com:8440/assets/blog/imgs/cover/"+newName);
            return "https://www.peteralbus.com:8440/assets/blog/imgs/cover/"+newName;
        } catch (IOException e) {
            return "上传错误:"+e.getMessage();
        }
    }
}
