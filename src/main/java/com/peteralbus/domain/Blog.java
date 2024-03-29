package com.peteralbus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * The type Blog.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Blog implements Serializable {
    /**
     * The Blog id.
     */
    @TableId(type= IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long blogId;
    /**
     * The Blog title.
     */
    String blogTitle;
    /**
     * The Blog img.
     */
    String blogImg;
    /**
     * The Blog type.
     */
    Integer blogType;
    /**
     * The Blog description.
     */
    String blogDescription;
    /**
     * The Blog author.
     */
    String blogAuthor;
    /**
     * The Blog content.
     */
    String blogContent;
    /**
     * The Blog time.
     */
    Date blogTime;
    /**
     * The Blog like.
     */
    Integer blogLike;
    /**
     * The Blog views.
     */
    Integer blogViews;
    /**
     * The Is top.
     */
    Integer isTop;
    /**
     * The Blog hide.
     */
    Boolean blogHide;
    /**
     * The Gmt modified.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    LocalDateTime gmtModified;
}
