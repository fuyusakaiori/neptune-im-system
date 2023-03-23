package com.example.nep.im.common.enums.status;

public enum NepGroupType {

    WECHAT(0, "微信群"),

    QQ(1, "QQ群");


    private final int type;

    private final String description;

    NepGroupType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isIllegalGroupType(int type){
        for (NepGroupType groupType : NepGroupType.values()) {
            if (groupType.type == type){
                return true;
            }
        }
        return false;
    }
}
