package com.fuyusakaiori.nep.im.service.core.friendship.controller;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepBlackFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friendship/black")
public class NepFriendshipBlackController {

    @Autowired
    private INepFriendshipBlackService friendshipBlackService;

    @PostMapping("/add")
    public NepBlackFriendshipResponse addFriendInBlackList(@RequestBody NepAddFriendshipBlackRequest request){
        log.info("NepFriendshipBlackController addFriendInBlackList: 开始执行拉黑好友 - request: {}", request);
        NepBlackFriendshipResponse response = friendshipBlackService.addFriendInBlackList(request);
        log.info("NepFriendshipBlackController addFriendInBlackList: 好友拉黑结束 - request: {}, response: {}", request, response);
        return response;
    }
    @PostMapping("/remove")
    public NepBlackFriendshipResponse removeFriendInBlackList(@RequestBody NepRemoveFriendshipBlackRequest request){
        log.info("NepFriendshipBlackController removeFriendInBlackList: 开始执行拉黑好友 - request: {}", request);
        NepBlackFriendshipResponse response = friendshipBlackService.removeFriendInBlackList(request);
        log.info("NepFriendshipBlackController removeFriendInBlackList: 好友拉黑结束 - request: {}, response: {}", request, response);
        return response;
    }


}
