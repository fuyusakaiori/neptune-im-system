package com.fuyusakaiori.nep.im.codec.serial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
class NepJackSonSerializer implements INepSerializer{

    private static final ThreadLocal<ObjectMapper> MAPPER_LOCAL =
            ThreadLocal.withInitial(ObjectMapper::new);

    @Override
    public byte[] serialize(Object source) {
        try {
            return MAPPER_LOCAL.get().writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            log.error("NepJackSonSerializer serialize: Jackson 序列化失败", e);
            return null;
        }
    }

    @Override
    public <T> T deserialize(byte[] source, Class<T> clazz) {
        try {
            return MAPPER_LOCAL.get().readValue(source, clazz);
        } catch (IOException e) {
            log.error("NepJackSonSerializer serialize: Jackson 反序列化失败", e);
            return null;
        }
    }
}
