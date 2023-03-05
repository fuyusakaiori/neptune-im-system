package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepRemoveFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupMemberResponse;

public interface INepFriendshipGroupMemberService {

    NepModifyFriendshipGroupMemberResponse addFriendshipGroupMember(NepAddFriendshipGroupMemberRequest request);

    NepModifyFriendshipGroupMemberResponse removeFriendshipGroupMember(NepRemoveFriendshipGroupMemberRequest request);

}
