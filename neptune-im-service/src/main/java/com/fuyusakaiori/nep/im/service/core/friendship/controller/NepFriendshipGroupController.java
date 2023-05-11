package com.fuyusakaiori.nep.im.service.core.friendship.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepCreateFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepDeleteFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryAllFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friendship/group")
public class NepFriendshipGroupController {

    @Autowired
    private INepFriendshipGroupService friendshipGroupService;

    /**
     * <h3>完成</h3>
     */
    @GetMapping(value = "/query-all")
    public NepQueryAllFriendshipGroupResponse queryAllFriendshipGroup(@RequestParam("appId") Integer appId, @RequestParam("ownerId") Integer groupOwnerId){
        NepQueryAllFriendshipGroupRequest request = new NepQueryAllFriendshipGroupRequest()
                                                            .setHeader(new NepRequestHeader().setAppId(appId)).setGroupOwnerId(groupOwnerId);
        log.info("NepFriendshipGroupController queryAllFriendshipGroup: 开始查询所有好友分组 - request: {}", request);
        NepQueryAllFriendshipGroupResponse response = friendshipGroupService.queryAllFriendshipGroup(request);
        log.info("NepFriendshipGroupController queryAllFriendshipGroup: 查询所有好友分组结束 - request: {}, response: {}", request, response);
        return response;
    }

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/create")
    public NepCreateFriendshipGroupResponse createFriendshipGroup(@RequestBody NepCreateFriendshipGroupRequest request){
        log.info("NepFriendshipGroupController createFriendshipGroup: 开始创建好友分组 - request: {}", request);
        NepCreateFriendshipGroupResponse response = friendshipGroupService.createFriendshipGroup(request);
        log.info("NepFriendshipGroupController createFriendshipGroup: 创建好友分组结束 - request: {}, response: {}", request, response);
        return response;
    }

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/delete")
    public NepDeleteFriendshipGroupResponse deleteFriendshipGroup(@RequestBody NepDeleteFriendshipGroupRequest request){
        log.info("NepFriendshipGroupController deleteFriendshipGroup: 开始删除好友分组 - request: {}", request);
        NepDeleteFriendshipGroupResponse response = friendshipGroupService.deleteFriendshipGroup(request);
        log.info("NepFriendshipGroupController deleteFriendshipGroup: 删除好友分组结束 - request: {}, response: {}", request, response);
        return response;
    }

}
