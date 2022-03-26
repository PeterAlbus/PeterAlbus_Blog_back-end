package com.peteralbus.service;

import com.peteralbus.domain.User;

/**
 * The interface User service.
 *
 * @author PeterAlbus Created 2022/3/26.
 */
public interface UserService
{
    /**
     * Register long.
     *
     * @param user the user
     * @return the string
     */
    String register(User user);

    /**
     * Gets user by id.
     *
     * @param userId the user id
     * @return the user by id
     */
    User getUserById(Long userId);

    /**
     * Auth by mail user.
     *
     * @param userMail the user mail
     * @param password the password
     * @return the user
     */
    User authByMail(String userMail,String password);

    /**
     * Auth by phone user.
     *
     * @param userPhone the user phone
     * @param password  the password
     * @return the user
     */
    User authByPhone(String userPhone,String password);

    /**
     * Update user int.
     *
     * @param user the user
     * @return the int
     */
    int updateUser(User user);

    /**
     * Change password int.
     *
     * @param userId      the user id
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return the int
     */
    int changePassword(Long userId,String oldPassword,String newPassword);

}
