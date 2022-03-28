package com.peteralbus.service.impl;

import com.peteralbus.domain.Blog;
import com.peteralbus.mapper.BlogMapper;
import com.peteralbus.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Blog service.
 * @author PeterAlbus
 * Created on 2021/7/21.
 */
@Service
public class BlogServiceImpl implements BlogService
{

    private BlogMapper blogMapper;

    /**
     * Sets blog mapper.
     *
     * @param blogMapper the blog mapper
     */
    @Autowired
    public void setBlogMapper(BlogMapper blogMapper)
    {
        this.blogMapper = blogMapper;
    }

    @Override
    public List<Blog> queryAll()
    {
        return blogMapper.selectList(null);
    }

    @Override
    public Blog queryById(Long id)
    {
        return blogMapper.selectById(id);
    }

    @Override
    public int add(Blog blog)
    {
        blog.setBlogTime(new Date(System.currentTimeMillis()));
        blog.setGmtModified(LocalDateTime.now());
        return blogMapper.insert(blog);
    }

    @Override
    public int update(Blog blog)
    {
        blog.setGmtModified(LocalDateTime.now());
        return blogMapper.updateById(blog);
    }
}
