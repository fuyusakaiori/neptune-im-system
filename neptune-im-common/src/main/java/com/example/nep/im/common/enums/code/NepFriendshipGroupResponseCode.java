package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

public enum NepFriendshipGroupResponseCode implements INepBaseResponseCode {
    FRIEND_GROUP_OWNER_NOT_EXIST(6000, "创建分组的用户不存在"),

    FRIEND_GROUP_CREATE_FAIL(6001, "创建分组失败"),

    FRIEND_GROUP_NOT_EXIST(6002, "好友分组不存在"),

    FRIEND_GROUP_DELETE_FAIL(6003, "好友分组删除失败"),

    ;


    private final int code;

    private final String message;

    NepFriendshipGroupResponseCode(int code, String message)
    {
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
