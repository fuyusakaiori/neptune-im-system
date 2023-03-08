package com.fuyusakaiori.nep.im.service.core.group.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepGroup {

    /**
     * <h3>群组 ID</h3>
     */
    private Integer groupId;

    /**
     * <h3>群主 ID</h3>
     */
    private Integer groupOwnerId;

    /**
     * <h3>群号</h3>
     */
    private String groupNumber;

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

    /**
     * <h3>群组是否开启全局禁言: 0 表示没有, 1 表示开启</h3>
     */
    private Boolean isMute;

    /**
     * <h3>群组是否解散: 0 表示没有, 1 表示解散</h3>
     */
    private Boolean isDelete;

    private Long createTime;

    private Long updateTime;

    private String extra;

}
