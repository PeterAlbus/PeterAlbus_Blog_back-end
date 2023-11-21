package com.peteralbus.service.impl;

import com.peteralbus.domain.Photo;
import com.peteralbus.domain.Result;
import com.peteralbus.mapper.PhotoMapper;
import com.peteralbus.service.PhotoService;
import com.peteralbus.util.ResultUtil;
import com.peteralbus.util.TypeUtil;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The type Photo service.
 * @author PeterAlbus
 * Created on 2021/7/29.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhotoServiceImpl implements PhotoService
{
    static final String BASE_PATH = "/home/PeterAlbus/assets/";
    static final String BASE_URL = "https://file.peteralbus.com/assets/";
    private final PhotoMapper photoMapper;
    @Override
    public List<Photo> queryAll()
    {
        return photoMapper.selectList(null);
    }

    @Override
    public Photo queryById(Long id) {
        return photoMapper.selectById(id);
    }

    @Override
    public int add(Photo photo)
    {
        return photoMapper.insert(photo);
    }

    @Override
    public Map<String, String> savePhoto(MultipartFile file, String savePath, boolean isThumbnail) {
        String uploadPath = BASE_PATH + savePath + "/";
        String fileName = file.getOriginalFilename();
        String type= TypeUtil.getType(fileName);
        String newName= UUID.randomUUID().toString().replace("-", "").toLowerCase()+type;
        Map<String,String> urlMap = new HashMap<>();
        urlMap.put("url", null);
        urlMap.put("thumbnailUrl", null);
        if(TypeUtil.isImg(type)) {
            File dest = new File(uploadPath + newName);
            try {
                file.transferTo(dest);
                urlMap.put("url", BASE_URL + savePath + "/" + newName);
                if(isThumbnail) {
                    Thumbnails.of(dest).size(200, 300).toFile(uploadPath + newName + "_THUMB.jpg");
                    urlMap.put("thumbnailUrl", BASE_URL + savePath + "/" + newName + "_THUMB.jpg");
                }
            } catch (Exception e) {
                System.out.println("Img upload error:" + e.getMessage());
            }
        }
        return urlMap;
    }

    @Override
    public Result<?> deletePhotoByUrl(String photoUrl) {
        if(!photoUrl.startsWith(BASE_URL)) {
            return ResultUtil.error(400,"Not a deletable photo.");
        }
        String photoPath = photoUrl.replace(BASE_URL, BASE_PATH);
        File file = new File(photoPath);
        File thumbFile = new File(photoPath + "_THUMB.jpg");
        if(file.exists()) {
            boolean result = file.delete();
            if(!result) {
                return ResultUtil.error(500,"Unknown file system error.");
            }
            if(thumbFile.exists()) {
                boolean thumbResult = thumbFile.delete();
                if(!thumbResult) {
                    return ResultUtil.success(null, "Delete success, but thumbnail delete failed.");
                }
            }
            return ResultUtil.success(null, "Delete success.");
        }
        return ResultUtil.error(400,"File not exists.");
    }

    @Override
    public int deletePhotoById(Long photoId) {
        return photoMapper.deleteById(photoId);
    }
}
