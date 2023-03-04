package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepAddFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class NepFriendshipServiceTest {

    @Autowired
    private INepFriendshipService friendshipService;

    @Test
    public void addFriendship(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepAddFriendship friendship = new NepAddFriendship()
                                              .setFriendFromId(1)
                                              .setFriendToId(6)
                                              .setFriendshipSource("系统申请")
                                              .setFriendRemark("同学")
                                              .setAdditionalInfo("群聊");
        NepAddFriendshipRequest request = new NepAddFriendshipRequest().setRequestHeader(header).setRequestBody(friendship);
        NepModifyFriendshipResponse response = friendshipService.addFriendship(request);
        log.info("response: {}", response);
    }

    @Test
    public void editFriendship(){

    }

    @Test
    public void releaseFriendship(){

    }

    @Test
    public void releaseAllFriendship(){

    }

    @Test
    public void queryFriendshipById(){

    }

    @Test
    public void queryAllFriendship(){

    }

}
