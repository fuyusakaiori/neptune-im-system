package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

public enum NepGroupResponseCode implements INepBaseResponseCode {

    CREATE_GROUP_FAIL(8001, "创建群组失败"),
    UPDATE_GROUP_FAIL(8002, "更新群组信息失败"),
    DISSOLVE_GROUP_FAIL(8003, "解散群组失败"),
    MUTE_GROUP_FAIL(8004, "全局禁言开启失败"),
    TRANSFER_GROUP_FAIL(8005, "转让群主失败"),
    GROUP_NOT_EXIST(8006, "群组不存在"),
    UPDATE_GROUP_AVATAR_FAIL(8007, "更新群组头像失败");

    private final int code;

    private final String message;

    NepGroupResponseCode(int code, String message) {
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
