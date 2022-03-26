package com.peteralbus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peteralbus.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface User mapper.
 * @author PeterAlbus
 * Created on 2022/3/26
 */
@Mapper
public interface UserMapper extends BaseMapper<User>
{
}
