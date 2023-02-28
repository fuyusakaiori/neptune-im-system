package com.fuyusakaiori.neptune.im.service.core.user.entity.request;


import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NeptuneQueryUserByIdRequest
{

    private NeptuneRequestHeader header;

    private List<Integer> userIdList;
}
