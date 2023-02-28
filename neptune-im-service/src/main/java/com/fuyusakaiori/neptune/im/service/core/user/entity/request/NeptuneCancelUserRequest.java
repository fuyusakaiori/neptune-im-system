package com.fuyusakaiori.neptune.im.service.core.user.entity.request;


import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneCancelUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <h2>注销用户请求类</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NeptuneCancelUserRequest
{

    /**
     * <h3>请求头</h3>
     */
    private NeptuneRequestHeader requestHeader;

    /**
     * <h3>需要注销的用户集合</h3>
     */
    private List<NeptuneCancelUser> requestBody;


}
