package com.fuyusakaiori.nep.im.service.core.message.entity.response;

import com.fuyusakaiori.nep.im.service.core.message.entity.dto.NepChatGroupHistoryMessage;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepLoadChatGroupHistoryMessageResponse {

    private int code;

    private String message;

    private List<NepChatGroupHistoryMessage> historyMessageList;

}
