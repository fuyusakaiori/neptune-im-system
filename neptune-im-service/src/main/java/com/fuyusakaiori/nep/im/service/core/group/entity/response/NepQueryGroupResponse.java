package com.fuyusakaiori.nep.im.service.core.group.entity.response;

import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryGroupResponse {

    private int code;

    private String message;

    private List<NepGroup> groupList;

}
