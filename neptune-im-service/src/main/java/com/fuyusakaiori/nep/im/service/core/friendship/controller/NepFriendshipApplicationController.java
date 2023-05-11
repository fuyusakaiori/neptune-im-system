package com.fuyusakaiori.nep.im.service.core.friendship.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepSendFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepSendFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friendship/application")
public class NepFriendshipApplicationController {

    @Autowired
    private INepFriendshipApplicationService friendshipApplicationService;

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/approve")
    public NepApproveFriendshipApplicationResponse approveFriendshipApplication(@RequestBody NepApproveFriendshipApplicationRequest request){
        log.info("NepFriendshipApplicationController approveFriendshipApplication: 开始审批好友申请 - request: {}", request);
        NepApproveFriendshipApplicationResponse response = friendshipApplicationService.approveFriendshipApplication(request);
        log.info("NepFriendshipApplicationController approveFriendshipApplication: 好友申请审批结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/send")
    public NepSendFriendshipApplicationResponse sendFriendshipApplication(@RequestBody NepSendFriendshipApplicationRequest request){
        log.info("NepFriendshipApplicationController sendFriendshipApplication: 开始发送好友请求 - request: {}", request);
        NepSendFriendshipApplicationResponse response = friendshipApplicationService.sendFriendshipApplication(request);
        log.info("NepFriendshipApplicationController sendFriendshipApplication: 发送好友申请结束 - request: {}, response: {}", request, response);
        return response;
    }


}
