package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepFriendshipBlackCheckType;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepReleaseFriendshipResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class NepFriendshipBlackServiceTest {

    @Autowired
    private INepFriendshipBlackService friendshipBlackService;

    private static final NepRequestHeader HEADER = new NepRequestHeader().setAppId(1);

    @Test
    public void addFriendInBlackListTest(){

    }

    @Test
    public void removeFriendInBlackList(){

    }

    @Test
    public void checkFriendInBlackList(){

    }

}
