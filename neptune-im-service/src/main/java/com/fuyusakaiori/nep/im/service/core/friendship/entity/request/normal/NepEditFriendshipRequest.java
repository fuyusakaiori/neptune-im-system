package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepEditFriendship;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>更新好友备注的请求</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepEditFriendshipRequest
{

    private NepRequestHeader requestHeader;

    private NepEditFriendship requestBody;

}
