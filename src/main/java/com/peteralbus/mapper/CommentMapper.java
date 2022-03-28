package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Comment mapper.
 * @author PeterAlbus
 * Created on 2022/3/28
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment>
{
}
