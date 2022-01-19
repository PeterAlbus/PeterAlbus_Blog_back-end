package com.peteralbus.controller;

import com.peteralbus.domain.Photo;
import com.peteralbus.service.PhotoService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * The type Photo controller.
 * @author PeterAlbus
 * Created on 2021/7/29.
 */
@RestController
@RequestMapping("/photo")
@CrossOrigin
public class PhotoController
{
    /**
     * The Photo service.
     */
    PhotoService photoService;

    @Autowired
    public void setPhotoService(PhotoService photoService)
    {
        this.photoService = photoService;
    }

    /**
     * Query all list.
     *
     * @return the list
     */
    @GetMapping("/queryAll")
    List<Photo> queryAll()
    {
        return photoService.queryAll();
    }

    /**
     * Upload string.
     *
     * @param file    the file
     * @param imgName the img name
     * @return the string
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,String imgName)
    {
        System.out.println("fileUpload");
        String uploadPath="/home/PeterAlbus/assets/blog/imgs/photo/";
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        File dest = new File(uploadPath + fileName);
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            Thumbnails.of("/home/PeterAlbus/assets/blog/imgs/photo/"+fileName).size(200, 300).toFile("/home/PeterAlbus/assets/blog/imgs/photo/"+fileName+"_THUMB.jpg");
            System.out.println("上传成功，当前上传的文件保存在"+"https://file.peteralbus.com/assets/blog/imgs/photo/"+fileName);
            Photo photo=new Photo();
            photo.setImgSrc("https://file.peteralbus.com/assets/blog/imgs/photo/"+fileName);
            photo.setImgThumb("https://file.peteralbus.com/assets/blog/imgs/photo/"+fileName+"_THUMB.jpg");
            photo.setImgName(imgName);
            photoService.add(photo);
            return "success";
        } catch (IOException e) {
            //log.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        return "fail";
    }

    /**
     * Custom upload string.
     *
     * @param file     the file
     * @param path     the path
     * @param saveName the save name
     * @return the string
     */
    @PostMapping("/customUpload")
    public String customUpload(@RequestParam("file") MultipartFile file,String path,String saveName)
    {
        /*pathExample:blog/imgs/photo/*/
        String uploadPath="/home/PeterAlbus/assets/"+path;
        // 获取上传的文件名称
        saveName=saveName+".jpg";
        File dest = new File(uploadPath + saveName);
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            Thumbnails.of(uploadPath+ saveName).size(300, 300).toFile(uploadPath+ saveName +"_THUMB.jpg");
            return "https://file.peteralbus.com/assets/"+path+ saveName +"_THUMB.jpg";
        } catch (IOException e) {
            e.printStackTrace();
            return "error:"+e.getMessage();
        }
    }

    /**
     * Custom upload string.
     *
     * @param file     the file
     * @param path     the path
     * @return the string
     */
    @PostMapping("/uploadOriginImg")
    public String uploadOriginImg(@RequestParam("file") MultipartFile file,String path)
    {
        /*pathExample:blog/imgs/photo/*/
        String uploadPath="/home/PeterAlbus/assets/"+path;
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        File dest = new File(uploadPath + fileName);
        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            return "https://file.peteralbus.com/assets/"+path+fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "error:"+e.getMessage();
        }
    }
}
