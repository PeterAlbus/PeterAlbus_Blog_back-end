package com.peteralbus.util;

import com.peteralbus.domain.Photo;
import com.peteralbus.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileUtil {
    private final PhotoService photoService;
    static final String BASE_PATH = "/home/PeterAlbus/assets/";
    static final String BASE_URL = "https://file.peteralbus.com/assets/";

    public Integer cleanPhotoTrash() {
        // 列出BASE_PATH+blog/imgs/photo/文件夹下所有文件
        File folder = new File(BASE_PATH + "blog/imgs/photo/");
        File[] files = folder.listFiles();
        List<Photo> photoList = photoService.queryAll();
        int count = 0;
        if(files==null) {
            return 0;
        }
        for (File file : files) {
            boolean isExist = false;
            for (Photo photo : photoList) {
                if (file.getName().equals(photo.getImgSrc().replace(BASE_URL + "blog/imgs/photo/", ""))) {
                    isExist = true;
                    break;
                }
                if (file.getName().equals(photo.getImgThumb().replace(BASE_URL + "blog/imgs/photo/", ""))) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                boolean result = file.delete();
                if(result) {
                    count++;
                }
            }
        }
        return 0;
    }
}
