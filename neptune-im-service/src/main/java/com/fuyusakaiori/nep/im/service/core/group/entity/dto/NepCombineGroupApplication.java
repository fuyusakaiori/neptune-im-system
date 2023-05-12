package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCombineGroupApplication {

    private Integer groupApplicationId;

    private String groupApplicationAdditionalInfo;

    private String groupApplicationSource;

    private Integer groupApplicationApproveStatus;

    private Integer groupId;

    private String groupNumber;

    private String groupName;

    private Integer senderId;

    private String senderUserName;

    private String senderNickName;

    private String senderAvatarAddress;

}
