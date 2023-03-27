package com.example.nep.im.common.entity.proto;

import com.example.nep.im.common.entity.proto.message.*;
import com.example.nep.im.common.enums.message.NepChatGroupMessageType;
import com.example.nep.im.common.enums.message.NepChatMessageType;
import com.example.nep.im.common.enums.message.NepSystemMessageType;
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
        messageClass.put(NepChatMessageType.P2P_MESSAGE.getMessageType(), NepChatP2PMessage.class);
        messageClass.put(NepChatMessageType.P2P_MESSAGE_ACK.getMessageType(), NepChatAckMessage.class);
        messageClass.put(NepChatMessageType.P2P_MESSAGE_RECEIVE_ACK.getMessageType(), NepChatConfirmAckMessage.class);
        messageClass.put(NepChatGroupMessageType.GROUP_MESSAGE.getMessageType(), NepChatGroupMessage.class);
        messageClass.put(NepChatGroupMessageType.GROUP_MESSAGE_ACK.getMessageType(), NepChatAckMessage.class);
    }
}
