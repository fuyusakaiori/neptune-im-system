package com.example.nep.im.common.entity.proto;

import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
import com.example.nep.im.common.entity.proto.message.NepLogoutMessage;
import com.example.nep.im.common.enums.message.NepChatMessageType;
import com.example.nep.im.common.enums.message.NepSystemMessageType;
import com.example.nep.im.common.entity.proto.message.NepLoginMessage;
import com.example.nep.im.common.entity.proto.message.NepPingMessage;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@ToString
public abstract class NepMessageBody implements Serializable {

    private static final Map<Integer, Class<? extends NepMessageBody>> messageClass = new HashMap<>();

    private int appId;

    private int clientType;

    private int messageType;

    private String imei;

    public static Class<? extends NepMessageBody> getMessageClass(int messageType){
        return messageClass.get(messageType);
    }

    static {
        messageClass.put(NepSystemMessageType.LOGIN.getMessageType(), NepLoginMessage.class);
        messageClass.put(NepSystemMessageType.PING.getMessageType(), NepPingMessage.class);
        messageClass.put(NepSystemMessageType.LOGOUT.getMessageType(), NepLogoutMessage.class);
        messageClass.put(NepChatMessageType.MESSAGE_P2P.getMessageType(), NepChatP2PMessage.class);
    }
}
