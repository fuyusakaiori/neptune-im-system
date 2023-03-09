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
public class NepLogoutMessage extends NepMessageBody {



}
