package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipGroupMemberService implements INepFriendshipGroupMemberService {

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

}
