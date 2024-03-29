package com.example.nep.im.common.entity.proto.message;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepChatAckMessage extends NepMessageBody {
    /**
     * <h3>响应码</h3>
     */
    private int code;

    /**
     * <h3>响应信息</h3>
     */
    private String message;

    /**
     * <h3>消息 ID</h3>
     */
    private int messageId;

}
