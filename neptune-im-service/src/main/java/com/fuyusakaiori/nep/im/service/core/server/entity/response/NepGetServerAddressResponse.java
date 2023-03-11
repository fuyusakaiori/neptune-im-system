package com.fuyusakaiori.nep.im.service.core.server.entity.response;


import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepGetServerAddressResponse {

    private int port;

    private String address;

}
