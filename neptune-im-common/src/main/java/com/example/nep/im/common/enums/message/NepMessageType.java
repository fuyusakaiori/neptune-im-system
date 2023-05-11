package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepMessageType implements INepBaseMessageType {


    P2P_MESSAGE(1),

    GROUP_MESSAGE(2),

    USER_MESSAGE(3),

    FRIEND_MESSAGE(4);

    private final int type;

    NepMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
