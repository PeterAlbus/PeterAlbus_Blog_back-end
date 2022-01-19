package com.peteralbus.service;

import com.peteralbus.domain.Blog;

import java.util.List;


/**
 * The interface Blog service.
 * @author PeterAlbus
 * Created on 2021/7/21.
 */
public interface BlogService
{
    /**
     * Query all list.
     *
     * @return the list
     */
    List<Blog> queryAll();

    /**
     * Query by id blog.
     *
     * @param id the id
     * @return the blog
     */
    Blog queryById(Integer id);

    /**
     * Add int.
     *
     * @param blog the blog
     * @return the int
     */
    int add(Blog blog);

    /**
     * Update int.
     *
     * @param blog the blog
     * @return the int
     */
    int update(Blog blog);
}
