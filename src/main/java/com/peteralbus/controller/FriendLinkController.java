package com.peteralbus.controller;

import com.peteralbus.domain.FriendLink;
import com.peteralbus.domain.Result;
import com.peteralbus.service.FriendLinkService;
import com.peteralbus.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/friendLink")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendLinkController {
    private final FriendLinkService friendLinkService;

    @RequestMapping("/getFriendLinkList")
    Result<List<FriendLink>> getFriendLinkList() {
        return ResultUtil.success(friendLinkService.getFriendLinkList());
    }

    @RequestMapping("/addFriendLink")
    Result<?> addFriendLinkList(FriendLink friendLink) {
        int result = friendLinkService.addFriendLink(friendLink);
        if (result > 0) {
            return ResultUtil.success(friendLink);
        } else {
            return ResultUtil.error(500,"添加失败");
        }
    }
}
