package com.example.nep.im.common.enums.status;


public enum NepGroupMemberQueryType {

    JOIN(0),

    EXIT(1);

    private final int type;

    NepGroupMemberQueryType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isIllegalGroupMemberQueryType(int type){
        for (NepGroupMemberQueryType queryType : NepGroupMemberQueryType.values()) {
            if (queryType.type == type){
                return true;
            }
        }
        return false;
    }
}
