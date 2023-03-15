package com.example.nep.im.common.entity.proto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
public class NepProtocol implements Serializable {
    public static final int PROTOCOL_LENGTH = 28;

    public static final int PROTOCOL_VERSION = 1;

    private NepMessageHeader messageHeader;

    private NepMessageBody messageBody;

}
