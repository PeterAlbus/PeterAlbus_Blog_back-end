package com.peteralbus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.peteralbus.domain.Blog;
import com.peteralbus.mapper.BlogMapper;
import com.peteralbus.service.BlogService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogServiceImpl implements BlogService
{
    private final BlogMapper blogMapper;

    @Override
    public List<Blog> queryAll()
    {
        QueryWrapper<Blog> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("blog_hide",true);
        List<Blog> blogs = blogMapper.selectList(queryWrapper);
        for (Blog blog : blogs)
        {
            blog.setBlogContent("");
        }
        return blogs;
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
