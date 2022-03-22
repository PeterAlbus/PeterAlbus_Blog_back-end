package com.peteralbus.controller;

import com.peteralbus.domain.Blog;
import com.peteralbus.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        final Set<String> allowTypes = new HashSet<String>(){{
            add(".jpg");
            add(".jpeg");
            add(".png");
            add(".JPG");
            add(".JPEG");
            add(".PNG");
            add(".webp");
            add(".tif");
            add(".WEBP");
            add(".TIF");
        }};
        if(fileName!=null)
        {
            type=fileName.substring(fileName.lastIndexOf('.'));
        }
        if(allowTypes.contains(type))
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
