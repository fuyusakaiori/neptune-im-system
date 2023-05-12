package com.example.nep.im.common.entity.proto.message.group;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepMuteGroupMemberMessage extends NepMessageBody {

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
