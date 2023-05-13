package com.fuyusakaiori.nep.im.service.core.message.entity.request;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepLoadChatP2PHistoryMessageRequest {

    private NepRequestHeader header;

    private Integer senderId;

    private Integer receiverId;

}
