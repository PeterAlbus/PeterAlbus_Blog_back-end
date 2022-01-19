package com.peteralbus.service.impl;

import com.peteralbus.domain.Photo;
import com.peteralbus.mapper.PhotoMapper;
import com.peteralbus.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Photo service.
 * @author PeterAlbus
 * Created on 2021/7/29.
 */
@Service
public class PhotoServiceImpl implements PhotoService
{
    /**
     * The Photo mapper.
     */
    PhotoMapper photoMapper;

    @Autowired
    public void setPhotoMapper(PhotoMapper photoMapper)
    {
        this.photoMapper = photoMapper;
    }

    @Override
    public List<Photo> queryAll()
    {
        return photoMapper.selectList(null);
    }

    @Override
    public int add(Photo photo)
    {
        return photoMapper.insert(photo);
    }
}
