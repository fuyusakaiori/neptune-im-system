package com.fuyusakaiori.nep.im.service.util.check;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatGroupHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatP2PHistoryMessageRequest;

import java.util.Objects;

public class NepCheckChatHistoryMessageParamUtil
{
    public static boolean checkNepLoadChatP2PHistoryMessageRequestParam(NepLoadChatP2PHistoryMessageRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer senderId = request.getSenderId();
        Integer receiverId = request.getReceiverId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(senderId) || senderId <= 0 || Objects.isNull(receiverId) || receiverId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepLoadChatGroupHistoryMessageRequestParam(NepLoadChatGroupHistoryMessageRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer userId = request.getUserId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }
}
