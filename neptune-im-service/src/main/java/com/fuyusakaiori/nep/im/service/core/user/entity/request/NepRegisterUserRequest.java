package com.fuyusakaiori.nep.im.service.core.user.entity.request;


import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>新增用户的请求类</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepRegisterUserRequest
{

    /**
     * <h3>请求头</h3>
     */
    private NepRequestHeader requestHeader;

    /**
     * <h3>用户资料集合</h3>
     */
    NepRegisterUser requestBody;

}
