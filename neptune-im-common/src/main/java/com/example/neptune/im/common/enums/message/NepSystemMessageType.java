package com.example.neptune.im.common.enums.message;

import com.example.neptune.im.common.enums.INepBaseMessageType;

public enum NepSystemMessageType implements INepBaseMessageType {

    /**
     * <h3>登陆消息: 9000</h3>
     */
    LOGIN(0x2328),

    /**
     * <h3>登陆通知消息: 9001</h3>
     */
    LOGIN_ACK(0x2329),

    /**
     * <h3>登出消息：9003</h3>
     */
    LOG_OUT(0x232b),

    /**
     * <h3>多端互斥下线: 9002</h3>
     */
    MUTUAL_LOGIN(0x232a);

    private final int messageType;

    NepSystemMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public int getMessageType() {
        return this.messageType;
    }
}
