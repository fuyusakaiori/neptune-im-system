package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCreateGroup {

    private Integer groupOwnerId;

    private String groupNumber;

    private String groupName;

    private Integer groupType;

}
