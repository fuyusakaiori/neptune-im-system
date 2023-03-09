package com.fuyusakaiori.nep.im.codec.serial;

import com.fuyusakaiori.nep.im.codec.proto.NepMessageBody;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>序列化器</h3>
 */
@Slf4j
public abstract class NepSerializer {

    private static final Map<Integer, INepSerializer> serializer = new HashMap<>();

    private static final int kryo = 0x00;

    private static final int jackson = 0x1;

    private static final int hessian = 0x2;

    private static final int jdk = 0x3;

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
        // 2. 获取序列化算法并执行反序列化
        return serializer.get(serializeType)
                       .deserialize(dataSource, NepMessageBody.getMessageClass(messageType));
    }

    public static byte[] serialize(int serializeType, Object dataSource) {
        // 1. 检测消息中携带的序列化算法是否存在
        if (!serializer.containsKey(serializeType))
        {
            log.error("NepDefaultSerializer deserialize: 消息中采用的序列化方式不存在");
            return null;
        }
        // 2. 获取序列化算法并执行序列化
        return serializer.get(serializeType).serialize(dataSource);
    }

}
