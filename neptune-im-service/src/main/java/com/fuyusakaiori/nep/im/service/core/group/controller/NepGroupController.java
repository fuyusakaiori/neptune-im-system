package com.fuyusakaiori.nep.im.service.core.group.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.*;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/nep/group")
public class NepGroupController {

    @Autowired
    private INepGroupService groupService;


    @PostMapping(value = "/create")
    public NepCreateGroupResponse createGroup(@RequestBody NepCreateGroupRequest request){
        log.info("NepGroupController createGroup: 开始创建群聊 - request: {}", request);
        NepCreateGroupResponse response = groupService.createGroup(request);
        log.info("NepGroupController createGroup: 创建群聊结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/edit-info")
    public NepEditGroupResponse editGroup(@RequestBody NepEditGroupRequest request){
        log.info("NepGroupController editGroup: 开始更新群聊资料 - request: {}", request);
        NepEditGroupResponse response = groupService.editGroupInfo(request);
        log.info("NepGroupController editGroup: 群聊资料更新结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/upload-avatar")
    public NepUploadGroupAvatarResponse uploadGroupAvatar(@RequestBody NepUploadGroupAvatarRequest request){
        log.info("NepGroupController uploadGroupAvatar: 开始上传群聊头像 - request: {}", request);
        NepUploadGroupAvatarResponse response = groupService.updateGroupAvatar(request);
        log.info("NepGroupController uploadGroupAvatar: 上传群聊头像结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/dissolve")
    public NepDissolveGroupResponse dissolveGroup(@RequestBody NepDissolveGroupRequest request){
        log.info("NepGroupController dissolveGroup: 开始解散群聊 - request: {}", request);
        NepDissolveGroupResponse response = groupService.dissolveGroup(request);
        log.info("NepGroupController dissolveGroup: 群聊解散结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/mute")
    public NepMuteGroupResponse muteGroup(@RequestBody NepMuteGroupRequest request){
        log.info("NepGroupController muteGroup: 开始执行全局禁言 - request: {}", request);
        NepMuteGroupResponse response = groupService.muteGroupChat(request);
        log.info("NepGroupController muteGroup: 全局禁言执行结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/transfer")
    public NepTransferGroupOwnerResponse transferGroupOwner(@RequestBody NepTransferGroupOwnerRequest request){
        log.info("NepGroupController transferGroupOwner: 开始执行群主转让 - request: {}", request);
        NepTransferGroupOwnerResponse response = groupService.transferGroupOwner(request);
        log.info("NepGroupController muteGroup: 群主转让执行结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query")
    public NepQueryGroupResponse queryGroup(@RequestParam("appId") int appId, @RequestParam("clientType") int clientType, @RequestParam("imei") String imei,
                                            @RequestParam("groupId") int groupId){
        NepRequestHeader header = new NepRequestHeader().setAppId(appId)
                                          .setClientType(clientType).setImei(imei);
        NepQueryGroupRequest request = new NepQueryGroupRequest().setHeader(header).setGroupId(groupId);
        log.info("NepGroupController queryGroup: 开始查询群聊 - request: {}", request);
        NepQueryGroupResponse response = groupService.queryGroup(request);
        log.info("NepGroupController queryGroup: 查询群聊结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query-list")
    public NepQueryGroupListResponse queryGroupList(@RequestParam("appId") int appId, @RequestParam("clientType") int clientType, @RequestParam("imei") String imei,
                                                @RequestParam("groupNumber") String groupNumber, @RequestParam("groupName") String groupName){
        NepRequestHeader header = new NepRequestHeader().setAppId(appId)
                                          .setClientType(clientType).setImei(imei);
        NepQueryGroupListRequest request = new NepQueryGroupListRequest().setHeader(header)
                                               .setGroupNumber(groupNumber).setGroupName(groupName);
        log.info("NepGroupController queryGroup: 开始查询群聊 - request: {}", request);
        NepQueryGroupListResponse response = groupService.queryGroupList(request);
        log.info("NepGroupController queryGroup: 查询群聊结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query-join")
    public NepQueryAllJoinedGroupResponse queryAllJoinedGroup(@RequestParam("appId") int appId, @RequestParam("clientType") int clientType, @RequestParam("imei") String imei,
                                                              @RequestParam("userId") int userId){
        NepRequestHeader header = new NepRequestHeader().setAppId(appId)
                                          .setClientType(clientType).setImei(imei);
        NepQueryAllJoinedGroupRequest request = new NepQueryAllJoinedGroupRequest().setHeader(header).setUserId(userId);
        log.info("NepGroupController queryAllJoinedGroup: 开始查询用户所有已经加入的群聊 - request: {}", request);
        NepQueryAllJoinedGroupResponse response = groupService.queryAllJoinedGroup(request);
        log.info("NepGroupController queryAllJoinedGroup: 查询用户所有已经加入的群聊结束 - request: {}, response: {}", request, response);
        return response;
    }

}
