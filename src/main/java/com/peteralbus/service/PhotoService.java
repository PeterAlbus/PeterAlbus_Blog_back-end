package com.peteralbus.service;

import com.peteralbus.domain.Photo;

import java.util.List;


/**
 * The interface Photo service.
 * @author PeterAlbus
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
     * Add int.
     *
     * @param photo the photo
     * @return the int
     */
    int add(Photo photo);
}
