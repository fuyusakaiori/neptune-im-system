package com.fuyusakaiori.neptune.im.service.core.user.entity.request;

import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneEditUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>更新用户实体类请求</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NeptuneEditUserRequest
{

    private NeptuneRequestHeader requestHeader;

    private NeptuneEditUser requestBody;

}
