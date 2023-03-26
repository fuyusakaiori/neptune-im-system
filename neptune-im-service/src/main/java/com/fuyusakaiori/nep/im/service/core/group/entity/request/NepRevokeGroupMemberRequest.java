package com.fuyusakaiori.nep.im.service.core.group.entity.request;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepRevokeGroupMemberRequest {

    private NepRequestHeader header;

    private Integer userId;

    private Integer groupId;

    private Integer groupMemberId;

}
