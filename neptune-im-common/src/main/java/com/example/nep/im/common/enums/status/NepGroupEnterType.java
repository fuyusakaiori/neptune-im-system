package com.example.nep.im.common.enums.status;

public enum NepGroupEnterType {

    /**
     * 申请加入
     */
    APPLY(0),

    /**
     * 邀请加入
     */
    INVITE(1);

    private final int type;

    NepGroupEnterType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isIllegalGroupEnterType(int type){
        for (NepGroupEnterType enterType : NepGroupEnterType.values()) {
            if (enterType.type == type){
                return true;
            }
        }
        return false;
    }
}
