package com.fuyusakaiori.nep.im.codec.serial;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * <h3>Hessian 序列化</h3>
 */
@Slf4j
@SuppressWarnings("unchecked")
public class NepHessianSerializer implements INepSerializer {
    @Override
    public byte[] serialize(Object source) {
        byte[] target = null;
        Hessian2Output hessian = null;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()){
            // 1. 初始化流对象
            hessian = new Hessian2Output(output);
            // 2. 序列化对象
            hessian.writeObject(source);
            hessian.getBytesOutputStream().flush();
            hessian.completeMessage();
            hessian.close();
            // 3. 获取序列化后的字节数组
            target = output.toByteArray();
            return target;
        } catch (Exception e) {
            log.error("NepHessianSerializer serialize: Hessian 序列化失败", e);
            return target;
        }
    }

    @Override
    public <T> T deserialize(byte[] source, Class<T> clazz) {
        Object target = null;
        Hessian2Input hession = null;
        try {
            hession = new Hessian2Input(
                    new ByteArrayInputStream(source));
            target = hession.readObject();
            hession.close();
            return (T) target;
        } catch (Exception e) {
            log.error("NepHessianSerializer deserialize: Hessian 反序列化失败", e);
            return (T) target;
        }
    }
}
