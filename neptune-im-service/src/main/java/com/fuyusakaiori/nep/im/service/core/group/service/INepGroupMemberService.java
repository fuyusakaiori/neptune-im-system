package com.fuyusakaiori.nep.im.service.core.group.service;

import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.*;

public interface INepGroupMemberService {

    NepAddGroupMemberResponse addGroupMember(NepAddGroupMemberRequest request);

    NepUpdateGroupMemberResponse updateGroupMember(NepUpdateGroupMemberRequest request);

    NepChangeGroupMemberTypeResponse changeGroupMemberType(NepChangeGroupMemberTypeRequest request);

    NepMuteGroupMemberResponse muteGroupMemberChat(NepMuteGroupMemberRequest request);

    NepExitGroupMemberResponse exitGroupMember(NepExitGroupMemberRequest request);

    NepQueryAllGroupMemberResponse queryAllGroupMember(NepQueryAllGroupMemberRequest request);

}
