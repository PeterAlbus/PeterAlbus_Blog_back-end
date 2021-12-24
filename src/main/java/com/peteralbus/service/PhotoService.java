package com.peteralbus.service;

import com.peteralbus.domain.Photo;

import java.util.List;

/*Created on 2021/7/29.*/
/*@author PeterAlbus*/
public interface PhotoService
{
    List<Photo> queryAll();
    int add(Photo photo);
}
