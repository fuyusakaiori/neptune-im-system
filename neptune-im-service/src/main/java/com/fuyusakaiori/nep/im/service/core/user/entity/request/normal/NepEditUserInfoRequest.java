package com.fuyusakaiori.nep.im.service.core.user.entity.request.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>更新用户个人资料的请求</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepEditUserInfoRequest {

    private NepRequestHeader header;

    private Integer userId;

    private String nickname;

    private Integer gender;

    private Integer age;

    private Long birthday;

    private String location;

    private String selfSignature;

    private Integer friendshipAllowType;

}
