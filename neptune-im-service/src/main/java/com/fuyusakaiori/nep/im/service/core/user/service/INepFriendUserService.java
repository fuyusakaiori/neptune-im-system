package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryFriendUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;

public interface INepFriendUserService {

    /**
     * <h3>查询用户的特定好友</h3>
     */
    NepQueryFriendUserResponse queryFriendUser(NepQueryFriendUserRequest request);

    /**
     * <h3>查询用户的所有好友</h3>
     */
    NepQueryFriendUserResponse queryAllFriendUser(NepQueryAllFriendUserRequest request);


}
