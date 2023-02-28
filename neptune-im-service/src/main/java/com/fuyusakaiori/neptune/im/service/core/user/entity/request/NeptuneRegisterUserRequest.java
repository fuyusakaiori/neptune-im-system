package com.fuyusakaiori.neptune.im.service.core.user.entity.request;


import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneRegisterUser;
import lombok.Data;

import java.util.List;

/**
 * <h3>新增用户的请求类</h3>
 */
@Data
public class NeptuneRegisterUserRequest {

    /**
     * <h3>请求头</h3>
     */
    private NeptuneRequestHeader requestHeader;

    /**
     * <h3>用户资料集合</h3>
     */
    List<NeptuneRegisterUser> requestBody;

}
