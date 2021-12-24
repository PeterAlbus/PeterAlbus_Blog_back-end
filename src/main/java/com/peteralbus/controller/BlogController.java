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

/*Created on 2021/7/20.*/
/*@author PeterAlbus*/

@RestController
@CrossOrigin
public class BlogController
{
    @Autowired
    BlogService blogService;
    @GetMapping("/queryAll")
    public List<Blog> queryAll()
    {
        return blogService.queryAll();
    }
    @GetMapping("/queryById")
    public Blog queryById(Integer id)
    {
        return blogService.queryById(id);
    }
    @PostMapping("/add")
    public String add(Blog blog)
    {
        int status=-1;
        status=blogService.add(blog);
        if(status>0) return blog.toString();
        else return "fail";
    }
    @PostMapping("/update")
    public String update(Blog blog)
    {
        int status=-1;
        status=blogService.update(blog);
        if(status>0) return blog.toString();
        else return "fail";
    }
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
            //log.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        return "上传错误";
    }
}
