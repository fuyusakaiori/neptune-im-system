package com.fuyusakaiori.nep.im.service.core.message.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatGroupHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatP2PHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.response.NepLoadChatGroupHistoryMessageResponse;
import com.fuyusakaiori.nep.im.service.core.message.entity.response.NepLoadChatP2PHistoryMessageResponse;
import com.fuyusakaiori.nep.im.service.core.message.service.INepChatHistoryMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/nep/message/history")
public class NepChatHistoryMessageController {

    @Autowired
    private INepChatHistoryMessageService chatHistoryMessageService;

    @GetMapping(value = "/p2p")
    public NepLoadChatP2PHistoryMessageResponse loadChatP2PHistoryMessage(@Param("appId") Integer appId, @Param("clientType") Integer clientType, @Param("imei") String imei,
                                                                          @Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId){
        NepRequestHeader header = new NepRequestHeader().setAppId(appId)
                                          .setImei(imei).setClientType(clientType);
        NepLoadChatP2PHistoryMessageRequest request = new NepLoadChatP2PHistoryMessageRequest().setHeader(header)
                                                              .setSenderId(senderId).setReceiverId(receiverId);
        log.info("NepChatHistoryMessageController loadChatP2PHistoryMessage: 开始加载用户历史消息 - request: {}", request);
        NepLoadChatP2PHistoryMessageResponse response = chatHistoryMessageService.loadChatP2PHistoryMessage(request);
        log.info("NepChatHistoryMessageController loadChatP2PHistoryMessage: 加载用户历史消息结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/group")
    public NepLoadChatGroupHistoryMessageResponse loadChatGroupHistoryMessage(@Param("appId") Integer appId, @Param("clientType") Integer clientType, @Param("imei") String imei,
                                                                              @Param("groupId") Integer groupId, @Param("userId") Integer userId){
        NepRequestHeader header = new NepRequestHeader().setAppId(appId)
                                          .setImei(imei).setClientType(clientType);
        NepLoadChatGroupHistoryMessageRequest request = new NepLoadChatGroupHistoryMessageRequest()
                                                                .setHeader(header).setGroupId(groupId).setUserId(userId);
        log.info("NepChatHistoryMessageController loadChatGroupHistoryMessage: 开始加载群聊消息 - request: {}", request);
        NepLoadChatGroupHistoryMessageResponse response = chatHistoryMessageService.loadChatGroupHistoryMessage(request);
        log.info("NepChatHistoryMessageController loadChatGroupHistoryMessage: 加载群聊消息结束 - request: {}, response: {}", request, response);
        return response;
    }



}
