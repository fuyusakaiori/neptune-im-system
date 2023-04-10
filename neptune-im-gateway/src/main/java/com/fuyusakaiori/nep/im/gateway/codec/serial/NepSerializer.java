package com.fuyusakaiori.nep.im.gateway.codec.serial;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.message.NepLogoutMessage;
import com.example.nep.im.common.enums.message.NepSystemMessageType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h3>序列化器</h3>
 */
@Slf4j
public abstract class NepSerializer {

    private static final Map<Integer, INepSerializer> serializer = new HashMap<>();

    public static final int kryo = 0x00;

    public static final int jackson = 0x1;

    public static final int hessian = 0x2;

    public static final int jdk = 0x3;

    static {
        serializer.put(NepSerializer.jackson, new NepJackSonSerializer());
        serializer.put(NepSerializer.kryo, new NepKryoSerializer());
        serializer.put(NepSerializer.hessian,new NepHessianSerializer());
        serializer.put(NepSerializer.jdk, new NepJdkSerializer());
    }

    public static NepMessageBody deserialize(int serializeType, int messageType, byte[] dataSource){
        // 1. 检测消息中携带的序列化算法是否存在
        if (!serializer.containsKey(serializeType)){
            log.error("NepDefaultSerializer deserialize: 消息中采用的序列化方式不存在");
            return null;
        }
        // 2. 如果是不携带任何数据的请求, 就不要反序列化
        if (messageType == NepSystemMessageType.LOGOUT.getMessageType()){
            return new NepLogoutMessage();
        }
        // 3. 获取请求类型
        Class<? extends NepMessageBody> messageClass = NepMessageBody.getMessageClass(messageType);
        if (Objects.isNull(messageClass)){
            log.error("NepDefaultSerializer deserialize: 没有对应的消息类型, 无法反序列化");
            return null;
        }
        // 4. 获取序列化算法并执行反序列化
        return serializer.get(serializeType).deserialize(dataSource, messageClass);
    }

    public static byte[] serialize(int serializeType, Object dataSource) {
        // 1. 检测消息中携带的序列化算法是否存在
        if (!serializer.containsKey(serializeType)) {
            log.error("NepDefaultSerializer deserialize: 消息中采用的序列化方式不存在");
            return null;
        }
        // 2. 获取序列化算法并执行序列化
        return serializer.get(serializeType).serialize(dataSource);
    }

}
