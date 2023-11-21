package com.peteralbus.service;

import com.peteralbus.domain.Photo;
import com.peteralbus.domain.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * The interface Photo service.
 *
 * @author PeterAlbus  Created on 2021/7/29.
 */
public interface PhotoService
{
    /**
     * Query all list.
     *
     * @return the list
     */
    List<Photo> queryAll();

    /**
     * Query by id photo.
     *
     * @param id the id
     * @return the photo
     */
    Photo queryById(Long id);

    /**
     * Add int.
     *
     * @param photo the photo
     * @return the int
     */
    int add(Photo photo);

    /**
     * Save photo as file to server and return url.
     *
     * @param file    the file
     * @param savePath the save path
     *                 e.g. blog/imgs/photo/
     * @param isThumbnail whether save thumbnail
     * @return the map
     */
    Map<String,String> savePhoto(MultipartFile file, String savePath, boolean isThumbnail);

    /**
     * Delete photo by url.
     *
     * @param photoUrl the photo url
     */
    Result<?> deletePhotoByUrl(String photoUrl);

    /**
     * Delete photo by id (in database).
     *
     * @param photoId the photo id
     */
    int deletePhotoById(Long photoId);
}
