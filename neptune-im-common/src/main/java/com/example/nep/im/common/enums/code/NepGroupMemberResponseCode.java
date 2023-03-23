package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

public enum NepGroupMemberResponseCode implements INepBaseResponseCode {

    ADD_GROUP_MEMBER_FAIL(9001, "用户加入群组失败"),
    UPDATE_GROUP_MEMBER_FAIL(9002, "更新群组成员资料失败"),
    CHANGE_GROUP_MEMBER_TYPE_FAIL(9003, "改变群组成员类型失败"),
    MUTE_GROUP_MEMBER_FAIL(9004, "禁言成员失败"),
    REVOKE_GROUP_MEMBER_MUTE_FAIL(9005,"撤销用户禁言失败"),
    EXIT_GROUP_MEMBER_FAIL(9006, "退出群组失败"),
    GROUP_MEMBER_NOT_EXIST(9007, "查询的群成员不存在");

    private final int code;

    private final String message;

    NepGroupMemberResponseCode(int code, String message)
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
