package com.peteralbus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Background image.
 * Store background image information of blog.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BackgroundImage {
    /**
     * The Background id.
     */
    @TableId(type= IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long backgroundId;
    /**
     * The Background url.
     */
    String backgroundUrl;
    /**
     * The Background path.
     */
    String backgroundPath;
    /**
     * The Background description.
     */
    String backgroundDescription;
    /**
     * Whether put it into random list.
     */
    Boolean isShow;
}
