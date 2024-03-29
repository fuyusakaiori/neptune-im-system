package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

public enum NepFriendshipResponseCode implements INepBaseResponseCode
{

    FRIENDSHIP_NOT_EXIST(3000, "好友关系不存在"),

    FRIENDSHIP_RELEASE_FAIL(3001, "好友关系删除失败"),

    FRIENDSHIP_RELEASE_ALL_FAIL(3002, "删除所有好友关系失败"),

    FRIENDSHIP_IS_RELEASED(3003, "好友关系已经被删除"),

    FRIENDSHIP_IS_BAN(3004, "对方禁止添加好友"),

    FRIENDSHIP_ADD_FAIL(3005, "添加好友关系失败"),

    FRIENDSHIP_UPDATE_FAIL(3006, "更新好友关系失败"),

    FRIENDSHIP_EXIST(3007, "好友关系已经存在"),

    FRIENDSHIP_CHECK_FAIL(3008, "校验好友关系失败");

    private final int code;
    private final String message;

    NepFriendshipResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
