package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.domain.Blog;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Blog mapper.
 * @author PeterAlbus
 * Created on 2021/7/21
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog>
{
}
