package com.fuyusakaiori.nep.im.service.core.user.entity.request.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>查询用户请求类</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepQueryUserByUserNameRequest {

    private NepRequestHeader header;

    private String username;

}
