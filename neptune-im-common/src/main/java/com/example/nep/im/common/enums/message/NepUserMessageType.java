package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepUserMessageType implements INepBaseMessageType {

    USER_INFO_EDIT(4000),

    USER_AVATAR_EDIT(4001),

    USER_ONLINE_STATUS_CHANGE(4002),

    USER_ONLINE_STATUS_CHANGE_NOTIFY(4003),

    USER_ONLINE_STATUS_CHANGE_NOTIFY_SYNC(4004);

    private final int type;

    NepUserMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
