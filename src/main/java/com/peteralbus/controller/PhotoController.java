package com.peteralbus.controller;

import com.peteralbus.domain.Photo;
import com.peteralbus.service.PhotoService;
import com.peteralbus.util.TypeUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        String uploadPath="/home/PeterAlbus/assets/blog/imgs/photo/";
        String fileName = file.getOriginalFilename();
        String type=TypeUtil.getType(fileName);
        String newName=UUID.randomUUID().toString().replace("-", "").toLowerCase()+type;
        if(TypeUtil.isImg(type))
        {
            File dest = new File(uploadPath + newName);
            try {
                // 上传的文件被保存了
                file.transferTo(dest);
                Thumbnails.of("/home/PeterAlbus/assets/blog/imgs/photo/"+newName).size(200, 300).toFile("/home/PeterAlbus/assets/blog/imgs/photo/"+newName+"_THUMB.jpg");
                Photo photo=new Photo();
                photo.setImgSrc("https://file.peteralbus.com/assets/blog/imgs/photo/"+newName);
                photo.setImgThumb("https://file.peteralbus.com/assets/blog/imgs/photo/"+newName+"_THUMB.jpg");
                photo.setImgName(imgName);
                photoService.add(photo);
                return "success";
            } catch (IOException e) {
                return "上传错误:"+e.getMessage();
            }
        }
        return "typeError";
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
        String fileName=file.getOriginalFilename();
        String type=TypeUtil.getType(fileName);
        if(TypeUtil.isImg(type))
        {
            if(!"".equals(saveName))
            {
                saveName=saveName+type;
            }
            else
            {
                saveName=fileName;
            }
            File dest = new File(uploadPath + saveName);
            try {
                file.transferTo(dest);
                Thumbnails.of(uploadPath+ saveName).size(300, 300).toFile(uploadPath+ saveName +"_THUMB.jpg");
                return "https://file.peteralbus.com/assets/"+path+ saveName +"_THUMB.jpg";
            } catch (IOException e) {
                return "error:"+e.getMessage();
            }
        }
        return "typeError";
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
        String fileName = file.getOriginalFilename();
        String type=TypeUtil.getType(fileName);
        if(TypeUtil.isImg(type))
        {
            File dest = new File(uploadPath + fileName);
            try {
                // 上传的文件被保存了
                file.transferTo(dest);
                return "https://file.peteralbus.com/assets/"+path+fileName;
            } catch (IOException e) {
                return "error:"+e.getMessage();
            }
        }
        return "typeError";
    }
}
