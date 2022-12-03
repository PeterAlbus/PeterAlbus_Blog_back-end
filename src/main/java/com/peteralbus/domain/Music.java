package com.peteralbus.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Music.
 *
 * @author PeterAlbus  Created on 2022/6/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Music
{
    @TableId(type= IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long musicId;
    String name;
    String artist;
    String url;
    String cover;
    String lrc;
}
