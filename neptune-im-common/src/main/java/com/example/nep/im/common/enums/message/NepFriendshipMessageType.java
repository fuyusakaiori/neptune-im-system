package com.example.nep.im.common.enums.message;

import com.example.nep.im.common.enums.INepBaseMessageType;

public enum NepFriendshipMessageType implements INepBaseMessageType {
    //添加好友
    FRIEND_ADD(3000),

    //更新好友
    FRIEND_UPDATE(3001),

    //删除好友
    FRIEND_REMOVE(3002),

    //好友申请
    FRIEND_REQUEST(3003),

    //好友申请已读
    FRIEND_APPLICATION_READ(3004),

    //好友申请审批
    FRIEND_APPLICATION_APPROVE(3005),

    //添加黑名单
    FRIEND_BLACK_ADD(3010),

    //移除黑名单
    FRIEND_BLACK_DELETE(3011),

    //新建好友分组
    FRIEND_GROUP_ADD(3012),

    //删除好友分组
    FRIEND_GROUP_DELETE(3013),

    //好友分组添加成员
    FRIEND_GROUP_MEMBER_ADD(3014),

    //好友分组移除成员
    FRIEND_GROUP_MEMBER_DELETE(3015),

    //删除所有好友
    FRIEND_ALL_REMOVE(3016);

    private final int type;

    NepFriendshipMessageType(int type) {
        this.type = type;
    }

    @Override
    public int getMessageType() {
        return type;
    }
}
