package com.fuyusakaiori.nep.im.service.core.group.entity.response;

import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepCombineGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryGroupResponse {

    private int code;

    private String message;

    private NepCombineGroup group;

}
