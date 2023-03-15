package com.fuyusakaiori.nep.im.service.core.user.entity.request.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>更新头像地址的请求类</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepEditUserAvatarRequest {

    private NepRequestHeader header;

    private Integer userId;

    private String avatarAddress;

}
