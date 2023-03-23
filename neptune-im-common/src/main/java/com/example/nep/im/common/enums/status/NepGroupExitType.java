package com.example.nep.im.common.enums.status;

public enum NepGroupExitType {

    /**
     * 主动退出
     */
    ACTIVE(0),

    /**
     * 被踢出
     */
    KICK_OUT(1),

    /**
     * 群组集散自动退出
     */
    DISSOLVE(2);


    private final int type;

    NepGroupExitType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isIllegalGroupExitType(int type){
        for (NepGroupExitType groupExitType : NepGroupExitType.values()) {
            if (groupExitType.type == type){
                return true;
            }
        }
        return false;
    }
}
