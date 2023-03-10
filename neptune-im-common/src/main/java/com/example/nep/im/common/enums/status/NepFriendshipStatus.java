package com.example.nep.im.common.enums.status;

public enum NepFriendshipStatus
{

    /**
     * <h3>表示好友关系正常</h3>
     */
    FRIENDSHIP_NORMAL(0),
    /**
     * <h3>表示好友关系已经被删除</h3>
     */
    FRIENDSHIP_RELEASE(1);


    private final int status;

    NepFriendshipStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
