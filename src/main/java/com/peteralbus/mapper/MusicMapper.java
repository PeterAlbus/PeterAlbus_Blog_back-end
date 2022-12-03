package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.domain.Music;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Friend link mapper.
 * @author PeterAlbus
 * Created on 2022/6/27.
 */
@Mapper
public interface MusicMapper extends BaseMapper<Music>
{
}
