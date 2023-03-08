package com.fuyusakaiori.nep.im.service.core.group.entity.request;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepCreateGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepEditGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepEditGroupRequest {

    private NepRequestHeader requestHeader;

    private NepEditGroup requestBody;

}
