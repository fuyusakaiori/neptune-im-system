package com.example.nep.im.common.entity.session;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepUserClientInfo {

    private int userId;

    private int appId;

    private int clientType;

    private String imei;

}
