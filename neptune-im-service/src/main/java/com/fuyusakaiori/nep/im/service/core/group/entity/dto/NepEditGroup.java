package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepEditGroup {

    /**
     * <h3>群组 ID</h3>
     */
    private Integer groupId;

    /**
     * <h3>群组名称</h3>
     */
    private String groupName;

    /**
     * <h3>群组简介</h3>
     */
    private String groupBriefInfo;

    /**
     * <h3>群头像</h3>
     */
    private String groupAvatarAddress;

    /**
     * <h3>群组类型: 0 表示微信群, 1 表示 QQ 群</h3>
     */
    private Integer groupType;

    /**
     * <h3>入群的申请方式: 0 表示任何都可以添加, 1 表示管理员审批添加, 2 表示禁止任何人添加</h3>
     */
    private Integer groupApplyType;

    /**
     * <h3>群组的成员最大数量</h3>
     */
    private Integer groupMaxCount;

}
