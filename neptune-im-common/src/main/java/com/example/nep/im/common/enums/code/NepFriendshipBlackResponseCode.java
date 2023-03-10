package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

public enum NepFriendshipBlackResponseCode implements INepBaseResponseCode {
    FRIEND_IS_BLACK(4001, "好友已经被拉黑"),

    FRIEND_BLACK_FAIL(4002, "好友拉黑失败"),

    FRIEND_IS_WHITE(4003, "好友未被拉黑"),

    FRIEND_WHITE_FAIL(4004, "好友撤销拉黑失败"),

    FRIEND_CHECK_BLACK_FAIL(4005, "校验好友拉黑状态失败"),

    FRIEND_BLACK_NOT_EXIST(4006, "用户拉黑的好友不存在");

    private final int code;

    private final String message;

    NepFriendshipBlackResponseCode(int code, String message) {
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
