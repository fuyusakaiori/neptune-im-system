package com.example.neptune.im.common.enums.reponse;

/**
 * <h3>基础响应信息</h3>
 */
public enum NeptuneBaseResponseCode implements INeptuneBaseResponseCode {

    SUCCESS(200, "请求处理成功"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    CHECK_PARAM_FAILURE(900, "参数校验失败"),

    UNKNOWN_ERROR(1000,"未知错误");

    private final int code;

    private final String message;

    NeptuneBaseResponseCode(int code, String message) {
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
