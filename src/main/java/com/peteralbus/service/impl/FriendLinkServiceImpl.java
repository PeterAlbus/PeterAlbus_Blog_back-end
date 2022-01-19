package com.peteralbus.service.impl;

import com.peteralbus.domain.FriendLink;
import com.peteralbus.mapper.FriendLinkMapper;
import com.peteralbus.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Friend link service.
 * @author PeterAlbus
 * Created on 2022/1/19.
 */
@Service
public class FriendLinkServiceImpl implements FriendLinkService
{
    /**
     * The Friend link mapper.
     */
    FriendLinkMapper friendLinkMapper;

    /**
     * Sets friend link mapper.
     *
     * @param friendLinkMapper the friend link mapper
     */
    @Autowired
    public void setFriendLinkMapper(FriendLinkMapper friendLinkMapper)
    {
        this.friendLinkMapper = friendLinkMapper;
    }

    @Override
    public int addFriendLink(FriendLink friendLink)
    {
        return friendLinkMapper.insert(friendLink);
    }

    @Override
    public int updateFriendLink(FriendLink friendLink)
    {
        return friendLinkMapper.updateById(friendLink);
    }

    @Override
    public List<FriendLink> getFriendLinkList()
    {
        return friendLinkMapper.selectList(null);
    }
}
