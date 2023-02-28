package com.fuyusakaiori.neptune.im.service.core.user.entity.request;

import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneQueryUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>查询用户请求类</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NeptuneQueryUserRequest {

    private NeptuneRequestHeader requestHeader;

    private NeptuneQueryUser requestBody;

}
