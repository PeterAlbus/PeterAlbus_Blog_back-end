package com.peteralbus.service.impl;

import com.peteralbus.domain.Blog;
import com.peteralbus.mapper.BlogMapper;
import com.peteralbus.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/*Created on 2021/7/21.*/
/*@author PeterAlbus*/
@Service
public class BlogServiceImpl implements BlogService
{
    @Autowired
    private BlogMapper blogMapper;
    @Override
    public List<Blog> queryAll()
    {
        return blogMapper.selectList(null);
    }

    @Override
    public Blog queryById(Integer id)
    {
        return blogMapper.selectById(id);
    }

    @Override
    public int add(Blog blog)
    {
        blog.setBlogTime(new Date(System.currentTimeMillis()));
        return blogMapper.insert(blog);
    }

    @Override
    public int update(Blog blog)
    {
        return blogMapper.updateById(blog);
    }
}
