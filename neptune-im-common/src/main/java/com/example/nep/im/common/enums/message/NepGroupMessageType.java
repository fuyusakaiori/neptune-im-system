package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepGroupMessageType implements INepBaseMessageType {

    GROUP_MEMBER_ADD(4001),

    GROUP_APPLICATION_SEND(4002);

    private final int type;

    NepGroupMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
