package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepGroupMessageType implements INepBaseMessageType {

    GROUP_MEMBER_ADD(4001),

    GROUP_APPLICATION_SEND(4002),

    GROUP_DISSOLVE(4003),

    GROUP_MUTE(4004),

    GROUP_TRANSFER(4005),

    GROUP_MUTE_MEMBER(4006),

    GROUP_EDIT(4007),

    GROUP_MEMBER_ADMIN(4008);

    private final int type;

    NepGroupMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
