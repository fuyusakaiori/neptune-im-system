package com.fuyusakaiori.nep.im.codec.proto.message;

import com.fuyusakaiori.nep.im.codec.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepLoginRequestMessage extends NepMessageBody {

    /**
     * <h3>用户 ID</h3>
     */
    private Integer userId;

}
