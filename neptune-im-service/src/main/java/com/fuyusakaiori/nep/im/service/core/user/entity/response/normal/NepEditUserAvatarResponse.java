package com.fuyusakaiori.nep.im.service.core.user.entity.response.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>更新头像地址的请求类</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepEditUserAvatarResponse {

    private NepUser newUser;

    private int code;

    private String message;

}
