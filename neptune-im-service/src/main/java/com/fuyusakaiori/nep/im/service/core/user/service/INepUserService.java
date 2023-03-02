package com.fuyusakaiori.nep.im.service.core.user.service;


import com.fuyusakaiori.nep.im.service.core.user.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepModifyUserResponse;

public interface INepUserService {

    NepQueryUserResponse queryFriendlyUserByIdList(NepQueryUserByIdListRequest request);

    NepQueryUserResponse queryDetailedUserById(NepQueryUserByIdRequest request);

    /**
     * <h3>更新用户资料</h3>
     */
    NepModifyUserResponse updateUser(NepEditUserRequest request);

    /**
     * <h3>批量新增用户</h3>
     */
    NepModifyUserResponse registerUser(NepRegisterUserRequest request);

    /**
     * <h3>删除用户资料</h3>
     * <h3>允许单次删除和批量删除</h3>
     */
    NepModifyUserResponse cancelUser(NepCancelUserRequest request);

    //============================== 管理后台可能会使用的方法 ==============================
    NepModifyUserResponse batchRegisterUser(NepImportUserRequest request);

}
