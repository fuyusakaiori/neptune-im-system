package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class NepFriendshipApplicationServiceTest {


    @Autowired
    private INepFriendshipApplicationService friendshipApplicationService;


    @Test
    public void approveFriendshipApplication(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepApproveFriendshipApplicationRequest request = new NepApproveFriendshipApplicationRequest()
                                                                 .setHeader(header)
                                                                 .setApproveStatus(NepFriendshipApplicationApproveStatus.AGREE.getStatus())
                                                                 .setApplyId(3);
        NepApproveFriendshipApplicationResponse response = friendshipApplicationService.approveFriendshipApplication(request);
        log.info("response: {}", response);
    }

}
