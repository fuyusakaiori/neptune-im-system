package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepAddFriendshipRequest
{

    private NepRequestHeader requestHeader;

    /**
     * <h3>好友关系接收者</h3>
     */
    private NepAddFriendship requestBody;

}
