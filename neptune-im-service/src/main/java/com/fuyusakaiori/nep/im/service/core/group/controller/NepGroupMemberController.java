package com.fuyusakaiori.nep.im.service.core.group.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.*;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/nep/group/member")
public class NepGroupMemberController {

    @Autowired
    private INepGroupMemberService groupMemberService;

    @PostMapping(value = "/add")
    public NepAddGroupMemberResponse addGroupMember(@RequestBody NepAddGroupMemberRequest request){
        log.info("NepGroupMemberController addGroupMember: 开始向群聊中添加成员 - request: {}", request);
        NepAddGroupMemberResponse response = groupMemberService.addGroupMember(request);
        log.info("NepGroupMemberController addGroupMember: 向群聊中添加成员结束 - request: {}, response: {}", request, response);
        return response;
    }


    @PostMapping(value = "/change")
    public NepChangeGroupMemberTypeResponse changeGroupMemberType(@RequestBody NepChangeGroupMemberTypeRequest request){
        log.info("NepGroupMemberController changeGroupMemberType: 开始变更群聊成员的类型 - request: {}", request);
        NepChangeGroupMemberTypeResponse response = groupMemberService.changeGroupMemberType(request);
        log.info("NepGroupMemberController changeGroupMemberType: 变更群聊成员类型结束 - request: {}, response: {}", request, response);
        return response;
    }


    @PostMapping(value = "/update-nickname")
    public NepUpdateGroupMemberResponse updateGroupMember(@RequestBody NepUpdateGroupMemberRequest request){
        log.info("NepGroupMemberController updateGroupMember: 开始变更群聊成员的昵称 - request: {}", request);
        NepUpdateGroupMemberResponse response = groupMemberService.updateGroupMember(request);
        log.info("NepGroupMemberController updateGroupMember: 变更群聊成员的昵称结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/mute")
    public NepMuteGroupMemberResponse muteGroupMember(@RequestBody NepMuteGroupMemberRequest request){
        log.info("NepGroupMemberController muteGroupMember: 开始变更群聊成员的昵称 - request: {}", request);
        NepMuteGroupMemberResponse response = groupMemberService.muteGroupMemberChat(request);
        log.info("NepGroupMemberController muteGroupMember: 变更群聊成员的昵称结束 - request: {}, response: {}", request, response);
        return response;
    }


    @PostMapping(value = "/exit")
    public NepExitGroupMemberResponse exitGroupMember(@RequestBody NepExitGroupMemberRequest request){
        log.info("NepGroupMemberController exitGroupMember: 群聊成员开始退出群聊 - request: {}", request);
        NepExitGroupMemberResponse response = groupMemberService.exitGroupMember(request);
        log.info("NepGroupMemberController exitGroupMember: 群聊成员退出群聊结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query")
    public NepQueryAllGroupMemberResponse queryAllGroupMember(@RequestParam("appId") int appId, @RequestParam("clientType") int clientType, @RequestParam("imei") String imei,
                                                              @RequestParam("groupId") int groupId, @RequestParam("queryType") int queryType){
        NepRequestHeader header = new NepRequestHeader().setAppId(appId)
                                          .setClientType(clientType).setImei(imei);
        NepQueryAllGroupMemberRequest request = new NepQueryAllGroupMemberRequest().setHeader(header)
                                                        .setGroupId(groupId).setQueryType(queryType);
        log.info("NepGroupMemberController queryAllGroupMember: 开始查询群聊中的所有成员 - request: {}", request);
        NepQueryAllGroupMemberResponse response = groupMemberService.queryAllGroupMember(request);
        log.info("NepGroupMemberController queryAllGroupMember: 查询群聊中的所有成员结束 - request: {}, response: {}", request, response);
        return response;
    }


}
