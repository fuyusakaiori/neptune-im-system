package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepMoveFriendshipGroupMemberRequest {

    private NepRequestHeader header;

    private Integer groupId;

    private List<Integer> groupMemberIdList;

}
