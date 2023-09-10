package com.peteralbus.config;

import cn.dev33.satoken.stp.StpInterface;
import com.peteralbus.domain.User;
import com.peteralbus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 *
 * @author PeterAlbus
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StpInterfaceImpl implements StpInterface {
    private final UserService userService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        User user = userService.getUserById(Long.valueOf((String) loginId));
        List<String> list = new ArrayList<String>();
        if (user.getUserIdentity() <= 0) {
            list.add(PermissionConfig.GOD);
            list.add(PermissionConfig.USER_MANAGEMENT);
            list.add(PermissionConfig.WRITE_ARTICLE);
            list.add(PermissionConfig.MODIFY_ARTICLE);
        }
        if (user.getUserIdentity() <= 1) {
            list.add(PermissionConfig.DELETE_COMMENT);
        }
        if (user.getUserIdentity() <= 5) {
            list.add(PermissionConfig.COMMENT);
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = userService.getUserById(Long.valueOf((String) loginId));
        List<String> list = new ArrayList<String>();
        if (user.getUserIdentity() == 0) {
            list.add(PermissionConfig.ROLE_OWNER);
        }
        if (user.getUserIdentity() == 1) {
            list.add(PermissionConfig.ROLE_ADMIN);
        }
        if (user.getUserIdentity() == 5) {
            list.add(PermissionConfig.ROLE_USER);
        }
        return list;
    }

}

