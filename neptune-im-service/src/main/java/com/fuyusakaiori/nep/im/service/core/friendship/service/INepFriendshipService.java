package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepCheckFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepQueryFriendshipResponse;

public interface INepFriendshipService {

    NepModifyFriendshipResponse addFriendship(NepAddFriendshipRequest request);

    NepModifyFriendshipResponse editFriendshipRemark(NepEditFriendshipRemarkRequest request);

    NepModifyFriendshipResponse releaseFriendship(NeptuneReleaseFriendshipRequest request);

    NepModifyFriendshipResponse releaseAllFriendship(NeptuneReleaseAllFriendshipRequest request);

    NepCheckFriendshipResponse checkFriendship(NepCheckFriendshipRequest request);


}
