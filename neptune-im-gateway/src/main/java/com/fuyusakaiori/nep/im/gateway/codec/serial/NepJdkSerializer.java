package com.fuyusakaiori.nep.im.gateway.codec.serial;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * <h3>JDK 序列化</h3>
 */
@Slf4j
@SuppressWarnings("unchecked")
public class NepJdkSerializer implements INepSerializer {

    @Override
    public byte[] serialize(Object source) {
        byte[] target = null;
        // 1. 初始化流对象
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)){
            // 2. 序列化
            oos.writeObject(source);
            oos.flush();
            // 3. 获取二进制数据
            target = baos.toByteArray();
            return target;
        } catch (IOException e) {
            log.error("NepJdkSerializer serialize: JDK 序列化失败", e);
            return target;
        }
    }

    @Override
    public <T> T deserialize(byte[] source, Class<T> clazz) {
        Object target = null;
        // 1. 初始化流对象
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(source))){
            // 2. 反序列化对象
            target = ois.readObject();
            return (T) target;
        } catch (IOException | ClassNotFoundException e) {
            log.error("NepJdkSerializer deserialize: JDK 反序列化失败", e);
            return (T) target;
        }
    }
}
