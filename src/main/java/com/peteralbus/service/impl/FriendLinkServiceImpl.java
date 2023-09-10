package com.peteralbus.service.impl;

import com.peteralbus.domain.FriendLink;
import com.peteralbus.mapper.FriendLinkMapper;
import com.peteralbus.service.FriendLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Friend link service.
 * @author PeterAlbus
 * Created on 2022/1/19.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendLinkServiceImpl implements FriendLinkService
{
    private final FriendLinkMapper friendLinkMapper;

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
