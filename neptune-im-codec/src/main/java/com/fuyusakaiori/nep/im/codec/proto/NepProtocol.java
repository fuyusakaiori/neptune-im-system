package com.fuyusakaiori.nep.im.codec.proto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
public class NepProtocol implements Serializable {
    public static final int MESSAGE_LENGTH = 28;

    private NepMessageHeader messageHeader;

    private NepMessageBody messageBody;

}
