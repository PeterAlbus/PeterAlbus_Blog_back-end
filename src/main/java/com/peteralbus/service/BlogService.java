package com.peteralbus.service;

import com.peteralbus.domain.Blog;

import java.util.List;

/*Created on 2021/7/21.*/
/*@author PeterAlbus*/
public interface BlogService
{
    List<Blog> queryAll();
    Blog queryById(Integer id);
    int add(Blog blog);
    int update(Blog blog);
}
