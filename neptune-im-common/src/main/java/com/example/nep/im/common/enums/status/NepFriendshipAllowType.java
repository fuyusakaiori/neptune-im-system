package com.example.nep.im.common.enums.status;

public enum NepFriendshipAllowType
{

    /**
     * <h3>任何人都可以添加</h3>
     */
    ANY(0),
    /**
     * <h3>验证后添加</h3>
     */
    VALIDATION(1),
    /**
     * <h3>禁止任何人添加好友</h3>
     */
    BAN(2);


    private final int type;

    NepFriendshipAllowType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
