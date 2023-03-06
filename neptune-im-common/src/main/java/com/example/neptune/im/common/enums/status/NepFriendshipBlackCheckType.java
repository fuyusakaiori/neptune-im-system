package com.example.neptune.im.common.enums.status;

public enum NepFriendshipBlackCheckType {

    SINGLE(0),

    DOUBLE(1);

    private final int type;

    NepFriendshipBlackCheckType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }
}
