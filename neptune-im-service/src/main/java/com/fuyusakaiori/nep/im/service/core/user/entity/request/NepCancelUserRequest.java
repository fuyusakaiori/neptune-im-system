package com.fuyusakaiori.nep.im.service.core.user.entity.request;


import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepCancelUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>注销用户请求类</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepCancelUserRequest
{

    /**
     * <h3>请求头</h3>
     */
    private NepRequestHeader requestHeader;

    /**
     * <h3>需要注销的用户集合</h3>
     */
    private NepCancelUser requestBody;


}
