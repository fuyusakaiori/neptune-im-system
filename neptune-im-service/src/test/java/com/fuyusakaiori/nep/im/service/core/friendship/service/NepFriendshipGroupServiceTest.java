package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryFriendshipGroupResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class NepFriendshipGroupServiceTest {

    @Autowired
    private INepFriendshipGroupService friendshipGroupService;


    private static final NepRequestHeader HEADER = new NepRequestHeader().setAppId(1);

    @Test
    public void createFriendshipGroupTest(){
        NepCreateFriendshipGroupRequest request = new NepCreateFriendshipGroupRequest()
                                                             .setGroupName("二次元老婆")
                                                             .setGroupOwnerId(1)
                                                             .setRequestHeader(HEADER);
        NepModifyFriendshipGroupResponse response = friendshipGroupService.createFriendshipGroup(request);
        log.info("response: {}", response);
    }

    @Test
    public void deleteFriendshipGroupTest(){
        NepDeleteFriendshipGroupRequest request = new NepDeleteFriendshipGroupRequest()
                                                          .setRequestHeader(HEADER)
                                                          .setGroupId(3);
        NepModifyFriendshipGroupResponse response = friendshipGroupService.deleteFriendshipGroup(request);
        log.info("response: {}", response);
    }

    @Test
    public void queryAllFriendshipGroupTest(){
        NepQueryAllFriendshipGroupRequest request = new NepQueryAllFriendshipGroupRequest()
                                                            .setGroupOwnerId(1)
                                                            .setRequestHeader(HEADER);
        NepQueryFriendshipGroupResponse response = friendshipGroupService.queryAllFriendshipGroup(request);
        log.info("response: {}", response);
    }

}
