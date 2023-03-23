package com.fuyusakaiori.nep.im.service.core.group.service;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepUploadGroupAvatarResponse {

    private int code;

    private String message;


}
