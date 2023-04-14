package com.fuyusakaiori.nep.im.service.core.group.entity.response;

import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepAddGroupMemberResponse {

    private int code;

    private String message;

    private NepJoinedGroup group;

}
