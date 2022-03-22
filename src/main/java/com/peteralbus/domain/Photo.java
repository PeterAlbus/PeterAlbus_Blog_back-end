package com.peteralbus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
/**
 * The type Photo.
 * @author PeterAlbus
 * Created on 2021/7/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Photo implements Serializable
{
    /**
     * The Img id.
     */
    @TableId(type= IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long imgId;
    /**
     * The Img name.
     */
    String imgName;
    /**
     * The Img src.
     */
    String imgSrc;
    /**
     * The Img thumb.
     */
    String imgThumb;
}
