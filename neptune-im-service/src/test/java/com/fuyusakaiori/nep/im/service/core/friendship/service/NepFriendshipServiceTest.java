package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepAddFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepEditFriendshipRemarkRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NeptuneReleaseAllFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NeptuneReleaseFriendshipRequest;
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
    public void addFriendshipTest(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepAddFriendship friendship = new NepAddFriendship()
                                              .setFriendFromId(1)
                                              .setFriendToId(5)
                                              .setFriendshipSource("系统推荐")
                                              .setFriendRemark("???")
                                              .setAdditionalInfo("你是谁");
        NepAddFriendshipRequest request = new NepAddFriendshipRequest()
                                                  .setRequestHeader(header)
                                                  .setRequestBody(friendship);
        NepModifyFriendshipResponse response = friendshipService.addFriendship(request);
        log.info("response: {}", response);
    }

    @Test
    public void editFriendshipRemarkTest(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepEditFriendshipRemarkRequest request = new NepEditFriendshipRemarkRequest()
                                                         .setRequestHeader(header)
                                                         .setFriendRemark("逆天的陌生人")
                                                         .setFriendFromId(1)
                                                         .setFriendToId(5);
        NepModifyFriendshipResponse response = friendshipService.editFriendshipRemark(request);
        log.info("response: {}", response);
    }

    @Test
    public void releaseFriendshipTest(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NeptuneReleaseFriendshipRequest request = new NeptuneReleaseFriendshipRequest()
                                                          .setRequestHeader(header)
                                                          .setFriendFromId(100)
                                                          .setFriendToId(6);
        NepModifyFriendshipResponse response = friendshipService.releaseFriendship(request);
        log.info("response: {}", response);
    }

    @Test
    public void releaseAllFriendship(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NeptuneReleaseAllFriendshipRequest request = new NeptuneReleaseAllFriendshipRequest()
                                                             .setRequestHeader(header)
                                                             .setFriendFromId(1);
        NepModifyFriendshipResponse response = friendshipService.releaseAllFriendship(request);
        log.info("response: {}", response);
    }

    @Test
    public void queryFriendshipById(){

    }

    @Test
    public void queryAllFriendship(){

    }

}
