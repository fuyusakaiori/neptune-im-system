package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendByAccountRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendByNameRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;

public interface INepFriendService
{

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


}
