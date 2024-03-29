package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>更新好友备注的请求</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepEditFriendshipRemarkRequest {

    private NepRequestHeader header;

    private Integer friendFromId;

    private Integer friendToId;

    private String friendRemark;

}
