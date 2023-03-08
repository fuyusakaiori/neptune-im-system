package com.fuyusakaiori.nep.im.service.core.group.entity.request;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepTransferGroupOwnerRequest {

    private NepRequestHeader requestHeader;

    private Integer groupId;

    private Integer targetOwnerId;
}
