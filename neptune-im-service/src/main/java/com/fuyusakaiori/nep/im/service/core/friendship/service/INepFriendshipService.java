package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepCheckFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepQueryFriendshipResponse;

public interface INepFriendshipService {

    NepModifyFriendshipResponse addFriendship(NepAddFriendshipRequest request);

    NepModifyFriendshipResponse editFriendship(NepEditFriendshipRequest request);

    NepModifyFriendshipResponse releaseFriendship(NeptuneReleaseFriendshipRequest request);

    NepModifyFriendshipResponse releaseAllFriendship(NeptuneReleaseAllFriendshipRequest request);

    NepCheckFriendshipResponse checkFriendship(NepCheckFriendshipRequest request);

    NepQueryFriendshipResponse queryFriendshipById(NepQueryFriendshipByIdRequest request);

    NepQueryFriendshipResponse queryAllFriendship(NepQueryAllFriendshipRequest request);


}
