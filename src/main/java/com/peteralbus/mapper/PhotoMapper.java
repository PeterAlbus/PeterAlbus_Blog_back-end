package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.domain.Photo;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Photo mapper.
 * @author PeterAlbus
 * Created on 2021/7/29.
 */
@Mapper
public interface PhotoMapper extends BaseMapper<Photo>
{
}
