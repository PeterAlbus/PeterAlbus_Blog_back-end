package com.peteralbus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.sql.Date;

/*Created on 2021/7/21.*/
/*@author PeterAlbus*/
public class Blog implements Serializable
{
    @TableId(type= IdType.AUTO)
    Integer blogId;
    String blogTitle;
    String blogImg;
    Integer blogType;
    String blogDescription;
    String blogAuthor;
    String blogContent;
    Date blogTime;
    Integer blogLike;
    Integer blogViews;
    Integer isTop;

    public Blog()
    {
    }

    public Blog(Integer blogId, String blogTitle, String blogImg, Integer blogType, String blogDescription, String blogAuthor, String blogContent, Date blogTime, Integer blogLike, Integer blogViews, Integer isTop)
    {
        this.blogId = blogId;
        this.blogTitle = blogTitle;
        this.blogImg = blogImg;
        this.blogType = blogType;
        this.blogDescription = blogDescription;
        this.blogAuthor = blogAuthor;
        this.blogContent = blogContent;
        this.blogTime = blogTime;
        this.blogLike = blogLike;
        this.blogViews = blogViews;
        this.isTop = isTop;
    }

    @Override
    public String toString()
    {
        return "Blog{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogImg='" + blogImg + '\'' +
                ", blogType=" + blogType +
                ", blogDescription='" + blogDescription + '\'' +
                ", blogAuthor='" + blogAuthor + '\'' +
                ", blogContent='" + blogContent + '\'' +
                ", blogTime=" + blogTime +
                ", blogLike=" + blogLike +
                ", blogViews=" + blogViews +
                ", isTop=" + isTop +
                '}';
    }

    public Integer getBlogId()
    {
        return blogId;
    }

    public void setBlogId(Integer blogId)
    {
        this.blogId = blogId;
    }

    public String getBlogTitle()
    {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle)
    {
        this.blogTitle = blogTitle;
    }

    public String getBlogImg()
    {
        return blogImg;
    }

    public void setBlogImg(String blogImg)
    {
        this.blogImg = blogImg;
    }

    public Integer getBlogType()
    {
        return blogType;
    }

    public void setBlogType(Integer blogType)
    {
        this.blogType = blogType;
    }

    public String getBlogDescription()
    {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription)
    {
        this.blogDescription = blogDescription;
    }

    public String getBlogAuthor()
    {
        return blogAuthor;
    }

    public void setBlogAuthor(String blogAuthor)
    {
        this.blogAuthor = blogAuthor;
    }

    public String getBlogContent()
    {
        return blogContent;
    }

    public void setBlogContent(String blogContent)
    {
        this.blogContent = blogContent;
    }

    public Date getBlogTime()
    {
        return blogTime;
    }

    public void setBlogTime(Date blogTime)
    {
        this.blogTime = blogTime;
    }

    public Integer getBlogLike()
    {
        return blogLike;
    }

    public void setBlogLike(Integer blogLike)
    {
        this.blogLike = blogLike;
    }

    public Integer getBlogViews()
    {
        return blogViews;
    }

    public void setBlogViews(Integer blogViews)
    {
        this.blogViews = blogViews;
    }

    public Integer getIsTop()
    {
        return isTop;
    }

    public void setIsTop(Integer isTop)
    {
        this.isTop = isTop;
    }
}
