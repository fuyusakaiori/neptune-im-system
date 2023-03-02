package com.fuyusakaiori.nep.im.service.core.user.entity.request;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
@ToString
public class NepImportUserRequest
{

    /**
     * <h3>请求头</h3>
     */
    private NepRequestHeader requestHeader;

    private List<NepRegisterUser> requestBody;

}
