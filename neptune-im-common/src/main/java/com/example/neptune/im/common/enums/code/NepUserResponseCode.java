package com.example.neptune.im.common.enums.code;

import com.example.neptune.im.common.enums.INepBaseResponseCode;

public enum NepUserResponseCode implements INepBaseResponseCode
{

    USER_NOT_EXIST(2000, "查询的用户不存在"),
    USER_LIST_EMPTY(2001, "查询结果为空"),
    REGISTER_USER_FAIL(2002, "注册用户失败"),
    EDIT_USER_FAIL(2003, "更新用户失败"),
    CANCEL_USER_FAIL(2004, "注销用户失败"),

    CANCEL_USER_NOT_EXIST(2005, "删除的用户不存在"),

    EDIT_USER_NOT_EXIST(2006, "更新的用户不存在");

    private final int code;

    private final String message;

    NepUserResponseCode(int code, String message) {
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
