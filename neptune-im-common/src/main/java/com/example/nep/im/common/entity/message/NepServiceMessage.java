package com.example.nep.im.common.entity.message;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepServiceMessage {

    private int targetId;

    private int appId;

    private int clientType;

    private String imei;

    private int messageType;

    private byte[] messageBody;

}
