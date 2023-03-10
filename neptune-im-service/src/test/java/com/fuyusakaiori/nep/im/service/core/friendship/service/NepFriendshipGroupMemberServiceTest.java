package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupMemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@Slf4j
@SpringBootTest
public class NepFriendshipGroupMemberServiceTest {

    @Autowired
    private INepFriendshipGroupMemberService friendshipGroupMemberService;

    private static final NepRequestHeader HEADER = new NepRequestHeader().setAppId(1);

    @Test
    public void addFriendshipGroupMemberTest(){
        NepAddFriendshipGroupMemberRequest request = new NepAddFriendshipGroupMemberRequest()
                                                                                        .setGroupId(4)
                                                                                        .setGroupMemberIdList(Arrays.asList(5, 6))
                                                                                        .setRequestHeader(HEADER);
        NepModifyFriendshipGroupMemberResponse response = friendshipGroupMemberService.addFriendshipGroupMember(request);
        log.info("response: {}", response);
    }


    @Test
    public void moveFriendshipGroupMemberTest(){
        NepMoveFriendshipGroupMemberRequest request = new NepMoveFriendshipGroupMemberRequest()
                                                              .setGroupId(4)
                                                              .setGroupMemberIdList(Arrays.asList(2, 3, 4))
                                                              .setRequestHeader(HEADER);
        NepModifyFriendshipGroupMemberResponse response = friendshipGroupMemberService.moveFriendshipGroupMember(request);
        log.info("response: {}", response);
    }

}
