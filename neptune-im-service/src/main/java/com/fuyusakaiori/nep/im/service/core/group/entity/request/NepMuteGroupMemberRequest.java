package com.fuyusakaiori.nep.im.service.core.group.entity.request;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepMuteGroupMemberRequest {

    private NepRequestHeader header;

    private Integer userId;

    private Integer groupId;

    private Integer groupMemberId;

    private Integer groupMemberType;

    private Long muteEndTime;

    /**
     * <h3>禁言成员还是撤销禁言</h3>
     */
    private Boolean mute;

}
