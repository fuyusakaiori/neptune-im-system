package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

public enum NepGroupApplicationCode implements INepBaseResponseCode {


    SEND_APPLICATION_FAIL(10001, "入群申请发送失败"),
    APPROVE_APPLICATION_FAIL(10002, "审批好友申请失败");

    private final int code;

    private final String message;

    NepGroupApplicationCode(int code, String message) {
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
