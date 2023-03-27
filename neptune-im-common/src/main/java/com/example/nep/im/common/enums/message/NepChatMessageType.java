package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepChatMessageType implements INepBaseMessageType {

    /**
     * <h3>单聊消息: 1103</h3>
     */
    P2P_MESSAGE(0x44F),

    /**
     * <h3>单聊消息 ack: 1046</h3>
     */
    P2P_MESSAGE_ACK(0x416),

    /**
     * <h3>消息接收者响应的 ACK</h3>
     */
    P2P_MESSAGE_RECEIVE_ACK(0x453);

    private final int type;


    NepChatMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
