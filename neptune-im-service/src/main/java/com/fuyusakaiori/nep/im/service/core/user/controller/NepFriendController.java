package com.fuyusakaiori.nep.im.service.core.user.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryAllFriendApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendResponse;
import com.fuyusakaiori.nep.im.service.core.user.service.INepFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friend")
public class NepFriendController {

    @Autowired
    private INepFriendService friendService;

    @GetMapping(value = "/query-all-request")
    public NepQueryAllFriendApplicationResponse queryAllFriendApplication(@RequestParam("appId") Integer appId, @RequestParam("userId") Integer userId){
       NepQueryAllFriendApplicationRequest request = new NepQueryAllFriendApplicationRequest()
                                                             .setHeader(new NepRequestHeader().setAppId(appId)).setUserId(userId);
        log.info("NepUserController queryAllFriendApplication: 开始查询所有好友申请- request: {}", request);
        NepQueryAllFriendApplicationResponse response = friendService.queryAllFriendApplication(request);
        log.info("NepUserController queryAllFriendApplication: 查询所有好友申请结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query-friend")
    public NepQueryFriendResponse queryFriend(@RequestParam("appId") Integer appId, @RequestParam("friendFromId") Integer friendFromId, @RequestParam("search") String search){
        NepQueryFriendRequest request = new NepQueryFriendRequest().setHeader(new NepRequestHeader().setAppId(appId))
                                                              .setFriendFromId(friendFromId).setUsername(search).setNickname(search).setFriendRemark(search);
        log.info("NepUserController queryFriend: 开始查询好友- request: {}", request);
        NepQueryFriendResponse response = friendService.queryFriend(request);
        log.info("NepUserController queryFriend: 查询好友结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query-all-friend")
    public NepQueryFriendResponse queryAllFriend(@RequestParam("appId") Integer appId, @RequestParam("friendFromId") Integer friendFromId){
        NepQueryAllFriendRequest request = new NepQueryAllFriendRequest()
                                                   .setHeader(new NepRequestHeader().setAppId(appId)).setFriendFromId(friendFromId);
        log.info("NepUserController queryAllFriend: 开始查询所有好友 - request: {}", request);
        NepQueryFriendResponse response = friendService.queryAllFriend(request);
        log.info("NepUserController queryAllFriend: 查询所有好友结束 - request: {}, response: {}", request, response);
        return response;
    }


}
