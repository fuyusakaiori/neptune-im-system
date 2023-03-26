package com.example.nep.im.common.enums.status;

public enum NepGroupJoinType {

    ANY(1),

    VALIDATION(2),

    BAN(3);


    private final int type;

    NepGroupJoinType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isIllegalGroupApplyType(int type){
        for (NepGroupJoinType applyType : NepGroupJoinType.values()) {
            if (applyType.type == type){
                return true;
            }
        }
        return false;
    }

}
