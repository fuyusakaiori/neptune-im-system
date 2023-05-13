package com.fuyusakaiori.nep.im.service.core.message.service;

import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatGroupHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatP2PHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.response.NepLoadChatP2PHistoryMessageResponse;
import com.fuyusakaiori.nep.im.service.core.message.entity.response.NepLoadChatGroupHistoryMessageResponse;

public interface INepChatHistoryMessageService {

    NepLoadChatP2PHistoryMessageResponse loadChatP2PHistoryMessage(NepLoadChatP2PHistoryMessageRequest request);

    NepLoadChatGroupHistoryMessageResponse loadChatGroupHistoryMessage(NepLoadChatGroupHistoryMessageRequest request);

}
