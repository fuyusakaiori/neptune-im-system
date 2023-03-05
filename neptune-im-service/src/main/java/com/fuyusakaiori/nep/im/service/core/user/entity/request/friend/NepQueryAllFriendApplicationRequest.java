package com.fuyusakaiori.nep.im.service.core.user.entity.request.friend;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>查询所有好友申请的请求</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllFriendApplicationRequest {

    /**
     * <h3>请求头</h3>
     */
    private NepRequestHeader requestHeader;

    /**
     * <h3>自己的 ID</h3>
     */
    private Integer userId;

}
