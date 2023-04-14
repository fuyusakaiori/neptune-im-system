package com.fuyusakaiori.nep.im.service.core.group.entity.response;

import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllJoinedGroupResponse {

    private int code;

    private String message;

    private List<NepJoinedGroup> groupList;

}
