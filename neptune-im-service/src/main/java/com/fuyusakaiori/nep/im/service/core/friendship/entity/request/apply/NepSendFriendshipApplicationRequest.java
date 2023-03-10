package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepSendFriendshipApplicationRequest {

    private NepRequestHeader requestHeader;

    private Integer friendFromId;

    private Integer friendToId;

    private String additionalInfo;

    private String remark;

    private String source;

}
