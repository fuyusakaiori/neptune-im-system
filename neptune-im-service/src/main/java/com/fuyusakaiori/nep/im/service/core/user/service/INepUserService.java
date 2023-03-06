package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepModifyUserResponse;

public interface INepUserService {


    /**
     * <h3>查询用户: 通过账号查询</h3>
     */
    NepQueryUserResponse queryUserByAccount(NepQueryUserByAccountRequest request);

    /**
     * <h3>查询用户: 通过用户昵称查询</h3>
     */
    NepQueryUserResponse queryUserByNickName(NepQueryUserByNickNameRequest request);

    /**
     * <h3>更新用户</h3>
     */
    NepModifyUserResponse updateUser(NepEditUserRequest request);

    /**
     * <h3>注册用户</h3>
     */
    NepModifyUserResponse registerUser(NepRegisterUserRequest request);

    /**
     * <h3>注销用户</h3>
     */
    NepModifyUserResponse cancelUser(NepCancelUserRequest request);

    //============================== 管理后台可能会使用的方法 ==============================
    NepModifyUserResponse batchRegisterUser(NepImportUserRequest request);

}
