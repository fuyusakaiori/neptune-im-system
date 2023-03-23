package com.example.nep.im.common.enums.status;

public enum NepGroupMemberType {

    LEADER(0, "群主"),

    ADMIN(1, "管理员"),

    MEMBER(2, "成员");


    private final int type;

    private final String description;

    NepGroupMemberType(int type, String description)
    {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isIllegalGroupMemberType(int type){
        for (NepGroupMemberType groupMemberType : NepGroupMemberType.values()) {
            if (groupMemberType.type == type){
                return true;
            }
        }
        return false;
    }
}
