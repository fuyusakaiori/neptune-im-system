package com.fuyusakaiori.nep.im.service.core.group.entity.request;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepCreateGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCreateGroupRequest {

    private NepRequestHeader requestHeader;

    private NepCreateGroup requestBody;

}
