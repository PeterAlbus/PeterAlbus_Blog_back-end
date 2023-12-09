package com.peteralbus.controller;

import com.peteralbus.domain.BackgroundImage;
import com.peteralbus.domain.Result;
import com.peteralbus.service.BackgroundImageService;
import com.peteralbus.service.PhotoService;
import com.peteralbus.service.impl.PhotoServiceImpl;
import com.peteralbus.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
@RequestMapping("/background")
public class BackgroundImageController {
    private final BackgroundImageService backgroundImageService;
    private final PhotoService photoService;

    @RequestMapping("/queryAll")
    public Result<List<BackgroundImage>> queryAll() {
        return ResultUtil.success(backgroundImageService.queryAll());
    }

    @RequestMapping("/queryById")
    public Result<BackgroundImage> queryById(Long backgroundId) {
        return ResultUtil.success(backgroundImageService.queryById(backgroundId));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestParam("file") MultipartFile file, String description) {
        BackgroundImage backgroundImage = new BackgroundImage();
        if(file.getSize() > 1024 * 1024 * 512) {
            return ResultUtil.error(500,"上传失败，文件大小超过512MB");
        }
        Map<String,String> savedUrl = photoService.savePhoto(file, "blog/static/background", false);
        backgroundImage.setBackgroundUrl(savedUrl.get("url"));
        backgroundImage.setBackgroundPath(savedUrl.get("url").replace(PhotoServiceImpl.BASE_URL,PhotoServiceImpl.BASE_PATH));
        int result = backgroundImageService.add(backgroundImage);
        if (result == 0) {
            return ResultUtil.error(500, "添加失败");
        }
        return ResultUtil.success(null);
    }

    @PostMapping("/update")
    public Result<?> update(Long backgroundId, String description) {
        BackgroundImage backgroundImage = backgroundImageService.queryById(backgroundId);
        backgroundImage.setBackgroundDescription(description);
        int result = backgroundImageService.update(backgroundImage);
        if (result == 0) {
            return ResultUtil.error(500, "更新失败");
        }
        return ResultUtil.success(null);
    }

    @PostMapping("/delete")
    public Result<?> delete(Long backgroundId) {
        BackgroundImage backgroundImage = backgroundImageService.queryById(backgroundId);
        Result<?> result = photoService.deletePhotoByUrl(backgroundImage.getBackgroundPath());
        if (result.getCode() != 200) {
            return result;
        }
        int infoResult = backgroundImageService.delete(backgroundId);
        if (infoResult == 0) {
            return ResultUtil.error(500, "数据库信息删除失败");
        }
        return ResultUtil.success(null);
    }
}
