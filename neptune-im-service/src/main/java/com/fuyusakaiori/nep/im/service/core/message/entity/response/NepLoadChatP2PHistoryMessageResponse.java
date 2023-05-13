package com.fuyusakaiori.nep.im.service.core.message.entity.response;

import com.fuyusakaiori.nep.im.service.core.message.entity.dto.NepChatP2PHistoryMessage;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepLoadChatP2PHistoryMessageResponse
{

    private int code;

    private String message;

    private List<NepChatP2PHistoryMessage> historyMessageList;

}
