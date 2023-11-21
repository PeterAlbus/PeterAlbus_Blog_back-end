package com.peteralbus.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.peteralbus.config.PermissionConfig;
import com.peteralbus.domain.Photo;
import com.peteralbus.domain.Result;
import com.peteralbus.service.PhotoService;
import com.peteralbus.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/photo")
@CrossOrigin
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/queryAll")
    public Result<List<Photo>> queryAll() {
        return ResultUtil.success(photoService.queryAll());
    }

    @RequestMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file, String imgName) {
        if(file.getSize() > 1024 * 1024 * 512) {
            return ResultUtil.error(500,"上传失败，文件大小超过512MB");
        }
        Map<String,String> savedUrl = photoService.savePhoto(file, "blog/imgs/photo", true);
        if(savedUrl == null || savedUrl.get("url") == null || savedUrl.get("thumbnailUrl") == null) {
            return ResultUtil.error(500,"上传失败（上传或压缩失败），请联系管理员");
        }
        Photo photo = new Photo();
        photo.setImgName(imgName);
        photo.setImgSrc(savedUrl.get("url"));
        photo.setImgThumb(savedUrl.get("thumbnailUrl"));
        int result = photoService.add(photo);
        if(result >= 1) {
            return ResultUtil.success(savedUrl);
        }
        return ResultUtil.error(500,"上传失败，请联系管理员");
    }

    @PostMapping("/uploadOriginImg")
    public Result<?> uploadOriginImg(@RequestParam("file") MultipartFile file, String path) {
        /*pathExample:blog/imgs/photo/*/
        // 文件大于1GB不允许上传
        if(file.getSize() > 1024 * 1024 * 512) {
            return ResultUtil.error(500,"上传失败，文件大小超过512MB");
        }
        Map<String,String> savedUrl = photoService.savePhoto(file, path, false);
        if(savedUrl == null || savedUrl.get("url") == null) {
            return ResultUtil.error(500,"上传失败，请联系管理员");
        }
        return ResultUtil.success(savedUrl.get("url"));
    }

    @GetMapping("/deletePhotoByUrl")
    public Result<?> deletePhotoByUrl(String photoUrl) {
        if(!StpUtil.hasPermission(PermissionConfig.DELETE_IMAGE)) {
            return ResultUtil.error(403, "没有删除图片的权限！");
        }
        return photoService.deletePhotoByUrl(photoUrl);
    }

    @GetMapping("/deletePhotoById")
    public Result<?> deletePhotoById(Long photoId) {
        if(!StpUtil.hasPermission(PermissionConfig.DELETE_IMAGE)) {
            return ResultUtil.error(403, "没有删除图片的权限！");
        }
        Photo photo = photoService.queryById(photoId);
        int result = photoService.deletePhotoById(photoId);
        Result<?> deleteResult = photoService.deletePhotoByUrl(photo.getImgSrc());
        if(result == 0) {
            return ResultUtil.error(500,"删除失败，请联系管理员");
        }
        if(deleteResult.getCode() != 200) {
            return ResultUtil.success(null, "删除成功，文件删除失败");
        }
        return ResultUtil.success(null, "删除成功");
    }
}
