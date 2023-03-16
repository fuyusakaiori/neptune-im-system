package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.*;

public interface INepUserService {


    NepRegisterUserResponse registerUser(NepRegisterUserRequest request);

    NepEditUserInfoResponse updateUserInfo(NepEditUserInfoRequest request);

    NepEditUserAvatarResponse updateUserAvatar(NepEditUserAvatarRequest request);

    NepCancelUserResponse cancelUser(NepCancelUserRequest request);

    NepLoginUserResponse loginUserInImSystem(NepLoginUserRequest request);

    NepQueryUserResponse queryUser(NepQueryUserRequest request);
}
