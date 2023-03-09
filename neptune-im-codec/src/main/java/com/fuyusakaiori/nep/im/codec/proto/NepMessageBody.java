package com.fuyusakaiori.nep.im.codec.proto;

import com.fuyusakaiori.nep.im.codec.proto.message.NepLoginRequestMessage;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@ToString
public abstract class NepMessageBody {

    /**
     * <h3>指令类型</h3>
     */
    private int messageType;

    private static final Map<Integer, Class<? extends NepMessageBody>> messageClass = new HashMap<>();

    /**
     * <h3>登陆请求</h3>
     */
    private static final int LoginRequestMessage = 0;

    public static Class<? extends NepMessageBody> getMessageClass(int messageType){
        return messageClass.get(messageType);
    }

    static {
        messageClass.put(LoginRequestMessage, NepLoginRequestMessage.class);
    }
}
