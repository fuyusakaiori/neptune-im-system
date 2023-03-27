package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

public enum NepMessageResponseCode implements INepBaseResponseCode {

    CHECK_RELATION_FAIL(10000, "发送消息的前置校验失败");


    private final int code;

    private final String message;

    NepMessageResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
