package com.fuyusakaiori.nep.im.service.core.group.entity.request;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepCreateGroupRequest {

    private NepRequestHeader header;

    private Integer groupOwnerId;

    private String groupName;

    private String groupBriefInfo;

    private Integer groupType;

    /**
     * <h3>创建群组时可以选择邀请好友直接加入</h3>
     */
    private List<Integer> groupMemberIdList;

}
