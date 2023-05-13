package com.fuyusakaiori.nep.im.service.core.message.entity.request;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepLoadChatGroupHistoryMessageRequest {

    private NepRequestHeader header;

    private Integer groupId;

    private Integer userId;
}
