package com.example.nep.im.common.enums.code;

import com.example.nep.im.common.enums.INepBaseResponseCode;

/**
 * <h3>基础响应信息</h3>
 */
public enum NepBaseResponseCode implements INepBaseResponseCode
{

    SUCCESS(200, "请求处理成功"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    CHECK_PARAM_FAIL(900, "参数校验失败"),

    UNKNOWN_ERROR(1000,"未知错误"),

    CALLBACK_FAIL(300, "回调失败");

    private final int code;

    private final String message;

    NepBaseResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage()
    {
        return this.message;
    }
}
