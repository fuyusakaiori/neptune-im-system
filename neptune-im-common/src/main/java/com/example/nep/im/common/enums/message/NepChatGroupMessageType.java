package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepChatGroupMessageType implements INepBaseMessageType {

    /**
     * <h3>群聊消息: 2104</h3>
     */
    GROUP_MESSAGE(0x838),

    /**
     * <h3>群聊消息已读: 2106</h3>
     */
    GROUP_MESSAGE_READ(0x83a),

    /**
     * <h3>群聊消息 ack: 2047</h3>
     */
    GROUP_MESSAGE_ACK(0x7ff);


    private final int type;

    NepChatGroupMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
