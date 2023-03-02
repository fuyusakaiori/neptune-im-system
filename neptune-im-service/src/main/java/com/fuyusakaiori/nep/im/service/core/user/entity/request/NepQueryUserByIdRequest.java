package com.fuyusakaiori.nep.im.service.core.user.entity.request;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryUserByIdRequest
{

    private NepRequestHeader requestHeader;

    private Integer userId;

}
