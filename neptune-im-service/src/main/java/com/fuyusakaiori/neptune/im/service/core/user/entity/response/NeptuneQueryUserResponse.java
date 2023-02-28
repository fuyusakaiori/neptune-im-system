package com.fuyusakaiori.neptune.im.service.core.user.entity.response;

import com.fuyusakaiori.neptune.im.service.core.user.entity.NeptuneUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NeptuneQueryUserResponse {

    private List<NeptuneUser> userList;

    private int code;

    private String message;

}
