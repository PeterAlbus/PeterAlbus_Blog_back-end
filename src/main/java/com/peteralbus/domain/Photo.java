package com.peteralbus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/*Created on 2021/7/29.*/
/*@author PeterAlbus*/
public class Photo implements Serializable
{
    @TableId(type= IdType.AUTO)
    Integer imgId;
    String imgName;
    String imgSrc;
    String imgThumb;

    public Photo()
    {
    }

    public Photo(Integer imgId, String imgName, String imgSrc, String imgThumb)
    {
        this.imgId = imgId;
        this.imgName = imgName;
        this.imgSrc = imgSrc;
        this.imgThumb = imgThumb;
    }

    @Override
    public String toString()
    {
        return "Photo{" +
                "imgId=" + imgId +
                ", imgName='" + imgName + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", imgThumb='" + imgThumb + '\'' +
                '}';
    }

    public String getImgThumb()
    {
        return imgThumb;
    }

    public void setImgThumb(String imgThumb)
    {
        this.imgThumb = imgThumb;
    }

    public Integer getImgId()
    {
        return imgId;
    }

    public void setImgId(Integer imgId)
    {
        this.imgId = imgId;
    }

    public String getImgName()
    {
        return imgName;
    }

    public void setImgName(String imgName)
    {
        this.imgName = imgName;
    }

    public String getImgSrc()
    {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc)
    {
        this.imgSrc = imgSrc;
    }
}
