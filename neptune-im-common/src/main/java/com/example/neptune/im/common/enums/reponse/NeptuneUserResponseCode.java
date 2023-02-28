package com.example.neptune.im.common.enums.reponse;

public enum NeptuneUserResponseCode implements INeptuneBaseResponseCode{

    REGISTER_USER_FAIL(2000, "注册用户失败"),
    QUERY_USER_RESULT_EMPTY(2001, "查询结果为空"),
    EDIT_USER_FAIL(2002, "更新用户失败"),

    CANCEL_USER_FAIL(2003, "注销用户失败");

    private final int code;

    private final String message;

    NeptuneUserResponseCode(int code, String message) {
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
