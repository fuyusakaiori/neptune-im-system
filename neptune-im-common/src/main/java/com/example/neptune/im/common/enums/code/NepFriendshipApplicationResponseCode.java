package com.example.neptune.im.common.enums.code;

import com.example.neptune.im.common.enums.INepBaseResponseCode;

public enum NepFriendshipApplicationResponseCode implements INepBaseResponseCode {

    SEND_FRIEND_APPLICATION_FAIL(5000, "发送好友申请失败"),

    FRIEND_APPLICATION_NOT_EXIST(5001, "好友请求不存在"),

    FRIEND_APPLICATION_APPROVED(5002, "好友请求已经审批过");

    private final int code;

    private final String message;

    NepFriendshipApplicationResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.getCode();
    }

    @Override
    public String getMessage() {
        return this.getMessage();
    }
}
