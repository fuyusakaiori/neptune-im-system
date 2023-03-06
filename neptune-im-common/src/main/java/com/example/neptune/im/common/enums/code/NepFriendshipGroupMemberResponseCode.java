package com.example.neptune.im.common.enums.code;

import com.example.neptune.im.common.enums.INepBaseResponseCode;

public enum NepFriendshipGroupMemberResponseCode implements INepBaseResponseCode {

    GROUP_MEMBER_CLEAR_FAIL(7000, "好友分组成员清空失败"),

    GROUP_MEMBER_ADD_FAIL(7001, "好友分组移入好友失败"),

    GROUP_MEMBER_MOVE_FAIL(7002, "变更用户所在好友分组失败"),

    GROUP_MEMBER_NOT_EXIST(7003, "好友不在该分组中");

    private final int code;

    private final String message;

    NepFriendshipGroupMemberResponseCode(int code, String message)
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
