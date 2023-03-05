package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;

public interface INepFriendService {

    /**
     * <h3>查询好友: 通过用户账号查询好友</h3>
     */
    NepQueryUserResponse queryFriendByAccount(NepQueryFriendByAccountRequest request);

    /**
     * <h3>查询好友: 通过用户昵称和备注查询好友</h3>
     */
    NepQueryUserResponse queryFriendByName(NepQueryFriendByNameRequest request);

    /**
     * <h3>查询用户的所有好友</h3>
     */
    NepQueryUserResponse queryAllFriend(NepQueryAllFriendRequest request);

    /**
     * <h3>查询所有被拉黑的好友</h3>
     */
    NepQueryUserResponse queryAllFriendBlackList(NepQueryAllFriendBlackRequest request);

    /**
     * <h3>查询所有给自己发送好友申请的用户</h3>
     */
    NepQueryUserResponse queryAllFriendApplication(NepQueryAllFriendApplicationRequest request);

    /**
     * <h3>查询所有好友分组中的成员</h3>
     */
    NepQueryUserResponse queryAllFriendGroupMember(NepQueryAllFriendGroupMemberRequest request);

}
