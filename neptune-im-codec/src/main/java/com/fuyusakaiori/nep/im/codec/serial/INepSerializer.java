package com.fuyusakaiori.nep.im.codec.serial;

public interface INepSerializer {

    byte[] serialize(Object source);

    <T> T deserialize(byte[] source, Class<T> clazz);

}
