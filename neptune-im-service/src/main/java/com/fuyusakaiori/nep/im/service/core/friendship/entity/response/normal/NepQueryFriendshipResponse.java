package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal;


import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryFriendshipResponse
{

    private List<NepFriendship> friendshipList;

    private int code;

    private String message;

}
