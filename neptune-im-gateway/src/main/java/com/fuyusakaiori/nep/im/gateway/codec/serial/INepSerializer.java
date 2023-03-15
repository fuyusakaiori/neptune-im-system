package com.fuyusakaiori.nep.im.gateway.codec.serial;

public interface INepSerializer {

    byte[] serialize(Object source);

    <T> T deserialize(byte[] source, Class<T> clazz);

}
