package com.peteralbus.controller;

import com.peteralbus.domain.FriendLink;
import com.peteralbus.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Friend link controller.
 * @author PeterAlbus
 * Created on 2022/1/19.
 */
@RestController
@CrossOrigin(origins = {"http://www.peteralbus.com","https://www.peteralbus.com","http://localhost","http://peteralbus.com","https://peteralbus.com"})
@RequestMapping("/friendLink")
public class FriendLinkController
{
    FriendLinkService friendLinkService;

    @Autowired
    public void setFriendLinkService(FriendLinkService friendLinkService)
    {
        this.friendLinkService = friendLinkService;
    }

    @RequestMapping("/getFriendLinkList")
    List<FriendLink> getFriendLinkList()
    {
        return friendLinkService.getFriendLinkList();
    }

    @RequestMapping("/addFriendLink")
    String addFriendLinkList(FriendLink friendLink)
    {
        int result=friendLinkService.addFriendLink(friendLink);
        if(result>0)
        {
            return friendLink.toString();
        }
        else
        {
            return "fail";
        }
    }
}
