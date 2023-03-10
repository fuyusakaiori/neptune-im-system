package com.fuyusakaiori.nep.im.codec.proto;

import com.example.nep.im.common.enums.message.NepSystemMessageType;
import com.fuyusakaiori.nep.im.codec.proto.message.NepLoginMessage;
import com.fuyusakaiori.nep.im.codec.proto.message.NepLogoutMessage;
import com.fuyusakaiori.nep.im.codec.proto.message.NepPingMessage;
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

    public static Class<? extends NepMessageBody> getMessageClass(int messageType){
        return messageClass.get(messageType);
    }

    static {
        messageClass.put(NepSystemMessageType.LOGIN.getMessageType(), NepLoginMessage.class);
        messageClass.put(NepSystemMessageType.PING.getMessageType(), NepPingMessage.class);
        messageClass.put(NepSystemMessageType.LOGOUT.getMessageType(), NepLogoutMessage.class);
    }
}
