package com.peteralbus.service.impl;

import com.peteralbus.domain.Photo;
import com.peteralbus.mapper.PhotoMapper;
import com.peteralbus.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*Created on 2021/7/29.*/
/*@author PeterAlbus*/
@Service
public class PhotoServiceImpl implements PhotoService
{
    @Autowired
    PhotoMapper photoMapper;
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
