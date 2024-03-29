package com.fuyusakaiori.nep.im.service.core.group.entity.response;


import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepSimpleGroupMember;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllGroupMemberResponse {

    private int code;

    private String message;

    private Map<Integer, List<NepSimpleGroupMember>> groupMemberMap;


}
