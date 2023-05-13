package com.fuyusakaiori.nep.im.service.core.message.service.impl;

import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.fuyusakaiori.nep.im.service.core.message.entity.dto.NepChatGroupHistoryMessage;
import com.fuyusakaiori.nep.im.service.core.message.entity.dto.NepChatP2PHistoryMessage;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatGroupHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatP2PHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.response.NepLoadChatGroupHistoryMessageResponse;
import com.fuyusakaiori.nep.im.service.core.message.entity.response.NepLoadChatP2PHistoryMessageResponse;
import com.fuyusakaiori.nep.im.service.core.message.service.INepChatHistoryMessageService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckChatHistoryMessageParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NepChatHistoryMessageService implements INepChatHistoryMessageService {

    @Autowired
    private NepChatHistoryMessageServiceImpl chatHistoryMessageServiceImpl;

    @Override
    public NepLoadChatP2PHistoryMessageResponse loadChatP2PHistoryMessage(NepLoadChatP2PHistoryMessageRequest request) {
        NepLoadChatP2PHistoryMessageResponse response = new NepLoadChatP2PHistoryMessageResponse();
        if (!NepCheckChatHistoryMessageParamUtil.checkNepLoadChatP2PHistoryMessageRequestParam(request)){
            response.setHistoryMessageList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepChatHistoryMessageService loadChatP2PHistoryMessage: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepChatP2PHistoryMessage> historyMessageList = chatHistoryMessageServiceImpl.doLoadChatP2PHistoryMessage(request);
            response.setHistoryMessageList(historyMessageList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepChatHistoryMessageService loadChatP2PHistoryMessage: 成功加载用户的历史聊天消息 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setHistoryMessageList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepChatHistoryMessageService loadChatP2PHistoryMessage: 加载用户聊天消息出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }

    @Override
    public NepLoadChatGroupHistoryMessageResponse loadChatGroupHistoryMessage(NepLoadChatGroupHistoryMessageRequest request) {
        NepLoadChatGroupHistoryMessageResponse response = new NepLoadChatGroupHistoryMessageResponse();
        if (!NepCheckChatHistoryMessageParamUtil.checkNepLoadChatGroupHistoryMessageRequestParam(request)){
            response.setHistoryMessageList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepChatHistoryMessageService loadChatGroupHistoryMessage: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepChatGroupHistoryMessage> historyMessageList = chatHistoryMessageServiceImpl.doLoadChatGroupHistoryMessage(request);
            response.setHistoryMessageList(historyMessageList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepChatHistoryMessageService loadChatGroupHistoryMessage: 成功加载群聊的聊天消息 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setHistoryMessageList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepChatHistoryMessageService loadChatGroupHistoryMessage: 加载群聊消息出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }
}
