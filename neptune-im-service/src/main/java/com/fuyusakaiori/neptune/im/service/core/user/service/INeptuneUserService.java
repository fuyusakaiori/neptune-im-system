package com.fuyusakaiori.neptune.im.service.core.user.service;


import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneCancelUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneRegisterUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneQueryUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneEditUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.response.NeptuneQueryUserResponse;
import com.fuyusakaiori.neptune.im.service.core.user.entity.response.NeptuneModifyUserResponse;

public interface INeptuneUserService {


    /**
     * <h3>查询用户资料</h3>
     * <h4>允许单个和批量查询两种方式</h4>
     */
    NeptuneQueryUserResponse queryUser(NeptuneQueryUserRequest request);

    /**
     * <h3>更新用户资料</h3>
     */
    NeptuneModifyUserResponse updateUser(NeptuneEditUserRequest request);

    /**
     * <h3>批量新增用户</h3>
     */
    NeptuneModifyUserResponse registerUser(NeptuneRegisterUserRequest request);

    /**
     * <h3>删除用户资料</h3>
     * <h3>允许单次删除和批量删除</h3>
     */
    NeptuneModifyUserResponse cancelUser(NeptuneCancelUserRequest request);

}
