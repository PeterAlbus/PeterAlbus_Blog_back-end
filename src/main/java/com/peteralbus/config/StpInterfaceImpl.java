package com.peteralbus.config;

import java.util.ArrayList;
import java.util.List;

import com.peteralbus.domain.User;
import com.peteralbus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.dev33.satoken.stp.StpInterface;

/**
 * 自定义权限验证接口扩展
 *
 * @author PeterAlbus
 */
@Component
public class StpInterfaceImpl implements StpInterface
{
    UserService userService;

    @Autowired
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType)
    {
        User user=userService.getUserById(Long.valueOf((String) loginId));
        List<String> list = new ArrayList<String>();
        if(user.getUserIdentity()<=0)
        {
            list.add("user-management");
            list.add("write-article");
            list.add("modify-article");
        }
        if(user.getUserIdentity()<=5)
        {
            list.add("comment");
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType)
    {
        User user=userService.getUserById(Long.valueOf((String) loginId));
        List<String> list = new ArrayList<String>();
        if(user.getUserIdentity()==0)
        {
            list.add("admin");
        }
        if(user.getUserIdentity()==5)
        {
            list.add("user");
        }
        return list;
    }

}

