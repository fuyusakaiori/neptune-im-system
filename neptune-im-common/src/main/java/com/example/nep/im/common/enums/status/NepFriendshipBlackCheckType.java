package com.example.nep.im.common.enums.status;

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

    public static boolean isIllegalBlackCheckType(int checkType){
        return checkType == SINGLE.getType() || checkType == DOUBLE.getType();
    }
}
