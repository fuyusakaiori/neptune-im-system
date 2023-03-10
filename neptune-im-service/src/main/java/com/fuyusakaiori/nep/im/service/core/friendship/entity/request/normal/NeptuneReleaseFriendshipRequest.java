package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>解除好友关系请求</h3>
 * <h4>1. 单个好友关系解除</h4>
 * <h4>2. 所有好友关系解除</h4>
 */
@Data
@Accessors(chain = true)
@ToString
public class NeptuneReleaseFriendshipRequest {

    private NepRequestHeader requestHeader;

    /**
     * <h3>好友关系的发起者</h3>
     */
    private Integer friendFromId;

    /**
     * <h3>好友关系的接收者</h3>
     */
    private Integer friendToId;

}
