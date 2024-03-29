package com.fuyusakaiori.nep.im.service.core.group.entity.request;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepUploadGroupAvatarRequest {

    private NepRequestHeader header;

    private Integer groupId;

    private String groupAvatarAddress;

}
