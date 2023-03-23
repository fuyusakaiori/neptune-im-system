package com.fuyusakaiori.nep.im.service.core.group.entity.response;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCreateGroupResponse {

    private int code;

    private String message;

}
