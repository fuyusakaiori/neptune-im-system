package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepChatMessageType implements INepBaseMessageType {

    //单聊消息 1103
    MESSAGE_P2P(0x44F),

    //单聊消息ACK 1046
    MESSAGE_ACK(0x416),

    //消息收到ack 1107
    MESSAGE_RECEIVE_ACK(1107),

    //发送消息已读   1106
    MESSAGE_READ(0x452),

    //消息已读通知给同步端 1053
    MESSAGE_READ_NOTIFY(0x41D),

    //消息已读回执，给原消息发送方 1054
    MESSAGE_READ_RECEIPT(0x41E),

    //消息撤回 1050
    MESSAGE_RECALL(0x41A),

    //消息撤回通知 1052
    MESSAGE_RECALL_NOTIFY(0x41C),

    //消息撤回回报 1051
    MESSAGE_RECALL_ACK(0x41B),

    ;

    private final int type;


    NepChatMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
