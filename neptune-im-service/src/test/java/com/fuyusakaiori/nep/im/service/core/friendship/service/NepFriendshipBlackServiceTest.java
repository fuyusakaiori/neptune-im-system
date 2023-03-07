package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.status.NepFriendshipBlackCheckType;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
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
        NepAddFriendshipBlackRequest request = new NepAddFriendshipBlackRequest()
                                                                            .setFriendFromId(1)
                                                                            .setFriendToId(2)
                                                                            .setRequestHeader(HEADER);
        NepModifyFriendshipResponse response = friendshipBlackService.addFriendInBlackList(request);
        log.info("response: {}", response);
    }

    @Test
    public void removeFriendInBlackList(){
        NepRemoveFriendshipBlackRequest request = new NepRemoveFriendshipBlackRequest()
                                                       .setFriendFromId(1)
                                                       .setFriendToId(2)
                                                       .setRequestHeader(HEADER);
        NepModifyFriendshipResponse response = friendshipBlackService.removeFriendInBlackList(request);
        log.info("response: {}", response);
    }

    @Test
    public void checkFriendInBlackList(){
        NepCheckFriendshipBlackRequest request = new NepCheckFriendshipBlackRequest()
                                                         .setCheckType(NepFriendshipBlackCheckType.DOUBLE.getType())
                                                         .setFriendFromId(1)
                                                         .setFriendToId(2)
                                                         .setRequestHeader(HEADER);
        NepCheckFriendshipBlackResponse response = friendshipBlackService.checkFriendInBlackList(request);
        log.info("response: {}", response);
    }

}
