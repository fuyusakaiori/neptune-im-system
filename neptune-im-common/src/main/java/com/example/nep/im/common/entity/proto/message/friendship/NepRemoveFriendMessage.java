package com.example.nep.im.common.entity.proto.message.friendship;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepRemoveFriendMessage extends NepMessageBody {

    /**
     * <h3>好友关系的发起者</h3>
     */
    private Integer friendFromId;

    /**
     * <h3>好友关系的接收者</h3>
     */
    private Integer friendToId;

}
