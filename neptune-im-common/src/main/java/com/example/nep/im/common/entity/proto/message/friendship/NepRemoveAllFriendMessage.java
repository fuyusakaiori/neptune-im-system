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
public class NepRemoveAllFriendMessage extends NepMessageBody {

    private Integer friendFromId;

}
