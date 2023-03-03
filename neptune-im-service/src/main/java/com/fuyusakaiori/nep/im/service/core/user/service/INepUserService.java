package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepModifyUserResponse;

public interface INepUserService {


    /**
     * <h3>查询某个用户的详细资料</h3>
     */
    NepQueryUserResponse queryDetailedUser(NepQueryUserRequest request);

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
