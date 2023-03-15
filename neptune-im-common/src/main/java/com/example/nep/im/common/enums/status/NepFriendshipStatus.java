package com.example.nep.im.common.enums.status;

public enum NepFriendshipStatus {

    /**
     * <h3>表示好友关系正常</h3>
     */
    FRIENDSHIP_NORMAL(0),
    /**
     * <h3>表示好友关系已经被删除</h3>
     */
    FRIENDSHIP_RELEASE(1),

    /**
     * <h3>表示 from 单方面删除 to</h3>
     */
    FRIENDSHIP_FROM_DELETE_TO(2),

    /**
     * <h3>表示 to 单方面删除 from</h3>
     */
    FRIENDSHIP_TO_DELETE_FROM(3);


    private final int status;

    NepFriendshipStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
