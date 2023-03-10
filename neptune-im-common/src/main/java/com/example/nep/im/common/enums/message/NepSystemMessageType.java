package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

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
    LOGOUT(0x232b),

    /**
     * <h3>多端互斥下线: 9002</h3>
     */
    MUTUAL_LOGIN(0x232a),

    /**
     * <h3>心跳检测</h3>
     */
    PING(0x270f),;

    private final int messageType;

    NepSystemMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public int getMessageType() {
        return this.messageType;
    }
}
