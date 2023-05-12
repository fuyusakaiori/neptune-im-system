package com.fuyusakaiori.nep.im.service.core.group.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepGroupApplication {

    private Integer groupApplyId;

    private Integer groupId;

    private Integer groupApplySenderId;

    private Integer groupApplyApproveUserId;

    private Integer groupApplyApproveStatus;

    private String groupApplyAdditionalInfo;

    private String groupApplySource;

    private Long createTime;

    private Long updateTime;

}
