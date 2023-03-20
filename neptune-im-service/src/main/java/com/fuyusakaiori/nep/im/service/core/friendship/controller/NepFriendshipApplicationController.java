package com.fuyusakaiori.nep.im.service.core.friendship.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friend/application")
public class NepFriendshipApplicationController {

    @Autowired
    private INepFriendshipApplicationService friendshipApplicationService;

    /**
     * <h3>完成</h3>
     */
    @GetMapping(value = "/approve")
    public NepApproveFriendshipApplicationResponse approveFriendshipApplication(@RequestParam("appId") Integer appId,
                                                                                @RequestParam("applyId") Integer applyId,
                                                                                @RequestParam("status") Integer status){
        NepApproveFriendshipApplicationRequest request = new NepApproveFriendshipApplicationRequest()
                                                                 .setHeader(new NepRequestHeader().setAppId(appId))
                                                                 .setApplyId(applyId).setApproveStatus(status);
        log.info("NepUserController approveFriendshipApplication: 开始查询好友请求 - request: {}", request);
        NepApproveFriendshipApplicationResponse response = friendshipApplicationService.approveFriendshipApplication(request);
        log.info("NepUserController approveFriendshipApplication: 查询好友请求结束 - request: {}, response: {}", request, response);
        return response;
    }


}
