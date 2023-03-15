package com.example.nep.im.common.enums.status;

public enum NepFriendshipCheckType {

    SINGLE(0),

    DOUBLE(1);

    private final int type;

    NepFriendshipCheckType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    public static boolean isIllegalFriendshipCheckType(int checkType){
        return checkType == SINGLE.getType() || checkType == DOUBLE.getType();
    }

}
