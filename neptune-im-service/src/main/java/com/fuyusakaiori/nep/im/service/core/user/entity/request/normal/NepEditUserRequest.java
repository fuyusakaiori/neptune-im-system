package com.fuyusakaiori.nep.im.service.core.user.entity.request.normal;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>更新用户实体类请求</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepEditUserRequest {

    private NepRequestHeader requestHeader;

    private NepEditUser requestBody;

}
