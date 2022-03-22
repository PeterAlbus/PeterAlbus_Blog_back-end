package com.peteralbus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Friend link.
 * @author PeterAlbus
 * Created on 2022/1/19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FriendLink
{
    /**
     * The Link id.
     */
    @TableId(type= IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long linkId;
    /**
     * The Link name.
     */
    String linkName;
    /**
     * The Link url.
     */
    String linkUrl;
}
