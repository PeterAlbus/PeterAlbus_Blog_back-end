package com.peteralbus.service;

import com.peteralbus.domain.FriendLink;

import java.util.List;


/**
 * The interface Friend link service.
 * @author PeterAlbus
 * Created on 2022/1/19.
 */
public interface FriendLinkService
{
    /**
     * Add friend link int.
     *
     * @param friendLink the friend link
     * @return the int
     */
    int addFriendLink(FriendLink friendLink);

    /**
     * Update friend link int.
     *
     * @param friendLink the friend link
     * @return the int
     */
    int updateFriendLink(FriendLink friendLink);

    /**
     * Gets link list.
     *
     * @return the link list
     */
    List<FriendLink> getFriendLinkList();
}
