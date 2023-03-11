package com.fuyusakaiori.nep.im.service.core.server.entity.request;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepGetServerAddressRequest {

    private int userId;

    private int appId;

    private int clientType;

}
