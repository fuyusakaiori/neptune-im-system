package com.fuyusakaiori.nep.im.service.core.user.entity.request.normal;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryUserByNickNameRequest {

    private NepRequestHeader requestHeader;

    private String nickName;
}