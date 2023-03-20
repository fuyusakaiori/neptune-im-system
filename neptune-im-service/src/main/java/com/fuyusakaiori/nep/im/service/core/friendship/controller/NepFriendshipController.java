package com.fuyusakaiori.nep.im.service.core.friendship.controller;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepAddFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepEditFriendshipRemarkRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepReleaseAllFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepReleaseFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepAddFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepEditFriendshipRemarkResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepReleaseFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friendship")
public class NepFriendshipController {

    @Autowired
    private INepFriendshipService friendshipService;

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/add")
    public NepAddFriendshipResponse addFriendship(@RequestBody NepAddFriendshipRequest request){
        log.info("NepFriendshipController addFriendship: 开始准备添加好友 - request: {}", request);
        NepAddFriendshipResponse response = friendshipService.addFriendship(request);
        log.info("NepFriendshipController addFriendship: 添加好友结束 - request: {}, response: {}", request, response);
        return response;
    }

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/release")
    public NepReleaseFriendshipResponse releaseFriendship(@RequestBody NepReleaseFriendshipRequest request){
        log.info("NepFriendshipController releaseFriendship: 开始准备删除好友 - request: {}", request);
        NepReleaseFriendshipResponse response = friendshipService.releaseFriendship(request);
        log.info("NepFriendshipController releaseFriendship: 添加好友结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/release-all")
    public NepReleaseFriendshipResponse releaseAllFriendship(@RequestBody NepReleaseAllFriendshipRequest request){
        log.info("NepFriendshipController releaseAllFriendship: 开始清空好友关系 - request: {}", request);
        NepReleaseFriendshipResponse response = friendshipService.releaseAllFriendship(request);
        log.info("NepFriendshipController releaseAllFriendship: 清空好友关系结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/edit-remark")
    public NepEditFriendshipRemarkResponse editFriendshipRemark(@RequestBody NepEditFriendshipRemarkRequest request){
        log.info("NepFriendshipController addFriendship: 开始准备更新好友备注 - request: {}", request);
        NepEditFriendshipRemarkResponse response = friendshipService.editFriendshipRemark(request);
        log.info("NepFriendshipController addFriendship: 添加好友结束 - request: {}, response: {}", request, response);
        return response;
    }


}
