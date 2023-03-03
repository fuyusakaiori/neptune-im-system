package com.example.neptune.im.common.enums.status;

public enum NepUserType
{

    /**
     * <h3>系统管理员</h3>
     */
    SYSTEM_ADMIN(0),
    /**
     * <h3>普通用户</h3>
     */
    NORMAL_USER(1);


    private final int type;

    NepUserType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
