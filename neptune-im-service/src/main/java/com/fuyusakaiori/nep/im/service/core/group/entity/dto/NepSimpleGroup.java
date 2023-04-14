package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepSimpleGroup
{

    private Integer groupId;

    private String groupNumber;

    private String groupName;

    private String groupAvatarAddress;

}
