package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;


import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <h3>管理后台可能会使用批量导入用户关系</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepImportFriendshipRequest
{

    private NepRequestHeader requestHeader;

    /**
     * <h3>好友关系发起者</h3>
     */
    private Integer friendFrom;

    /**
     * <h3>多个好友关系接收者</h3>
     */
    private List<NepAddFriendship> friendToList;

}
