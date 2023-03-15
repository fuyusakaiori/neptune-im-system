package com.fuyusakaiori.nep.im.gateway.codec.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * <h3>Kryo 序列化</h3>
 */
@Slf4j
@SuppressWarnings("unchecked")
public class NepKryoSerializer implements INepSerializer {
    /**
     * <h3>1. Kryo 是非线程安全的 </h3>
     * <h3>2. 采用 ThreadLocal 控制并发序列化</h3>
     * <h3>3. 局部变量确实可以控制, 但是每次序列化都需要创建对象, 浪费性能</h3>
     */
    private static final ThreadLocal<Kryo> KRYO_LOCAL = ThreadLocal.withInitial(()->{
        // 1. 准备 Kryo 对象
        Kryo kryo = new Kryo();
        // TODO 2. 添加注册器

        return kryo;
    });

    @Override
    public byte[] serialize(Object source) {
        byte[] target = null;
        // 1. 准备输出结果
        try (Output output = new Output(new ByteArrayOutputStream())){
            // 2. 序列化
            KRYO_LOCAL.get()
                    .writeClassAndObject(output, source);
            // 3. 从输出对象获取序列化结果
            target = output.toBytes();
            return target;
        } catch (Exception e) {
            log.error("NepKryoSerializer serialize: Kryo 序列化失败", e);
            return null;
        }
    }

    @Override
    public <T> T deserialize(byte[] source, Class<T> clazz) {
        Object target = null;
        // 1. 获取输入结果
        try (Input input = new Input(new ByteArrayInputStream(source))){
            // 2. 反序列化并且获取结果
            target = KRYO_LOCAL.get()
                             .readClassAndObject(input);
            return (T) target;
        } catch (Exception e) {
            log.error("NepKryoSerializer deserialize: Kryo 反序列化失败", e);
            return null;
        }
    }
}
