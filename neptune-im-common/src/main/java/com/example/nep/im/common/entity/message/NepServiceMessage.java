package com.example.nep.im.common.entity.message;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
public class NepServiceMessage implements Serializable {

    private int targetId;

    private int targetAppId;

    private int targetClientType;

    private String targetImei;

    private int messageType;

    private NepMessageBody messageBody;


}
