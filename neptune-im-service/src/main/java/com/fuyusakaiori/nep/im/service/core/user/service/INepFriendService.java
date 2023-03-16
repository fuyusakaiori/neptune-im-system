package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryAllFriendApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendResponse;

public interface INepFriendService {

    /**
     * <h3>查询用户的所有好友</h3>
     */
    NepQueryFriendResponse queryAllFriend(NepQueryAllFriendRequest request);

    /**
     * <h3>查询好友: 通过用户账号、昵称、备注查询好友</h3>
     */
    NepQueryFriendResponse queryFriend(NepQueryFriendRequest request);


    /**
     * <h3>查询所有给自己发送好友申请的用户</h3>
     */
    NepQueryAllFriendApplicationResponse queryAllFriendApplication(NepQueryAllFriendApplicationRequest request);

    /**
     * <h3>查询所有好友分组中的成员</h3>
     */
    NepQueryFriendGroupMemberResponse queryAllFriendGroupMember(NepQueryAllFriendGroupMemberRequest request);

}
