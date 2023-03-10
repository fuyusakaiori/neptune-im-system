package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.example.nep.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepSendFriendshipApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class NepFriendshipApplicationMapperTest {

    @Autowired
    private INepFriendshipApplicationMapper friendshipApplicationMapper;

    @Test
    public void sendFriendshipApplication(){
        for (int id = 19;id <= 22; id++){
            NepSendFriendshipApplication application = new NepSendFriendshipApplication()
                                                               .setFriendFromId(id)
                                                               .setFriendToId(18)
                                                               .setRemark("备注")
                                                               .setAdditionalInfo("我要超市你")
                                                               .setSource("网络搜索");
            int result = friendshipApplicationMapper.sendFriendshipApplication(1, application, System.currentTimeMillis(), System.currentTimeMillis());
            log.info("result: {}", result);
        }
    }

    @Test
    public void updateFriendshipApplication(){
        NepSendFriendshipApplication application = new NepSendFriendshipApplication()
                                                           .setFriendFromId(18)
                                                           .setFriendToId(19)
                                                           .setRemark("新的备注")
                                                           .setAdditionalInfo("删除后又重新申请好友")
                                                           .setSource("网络搜索");
        int result = friendshipApplicationMapper.updateFriendshipApplication(1, application, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void approveFriendshipApplication(){
        int result = friendshipApplicationMapper.approveFriendshipApplication(1, 2,
                NepFriendshipApplicationApproveStatus.AGREE.getStatus(), System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void readAllFriendshipApplication(){
        int result = friendshipApplicationMapper.readAllFriendshipApplication(1, Arrays.asList(2, 3, 4, 5, 6), System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void queryFriendshipApplicationById(){
        NepFriendshipApplication application = friendshipApplicationMapper.queryFriendshipApplicationById(1, 1);
        log.info("application: {}", application);
    }

    @Test
    public void queryFriendshipApplicationByUserId(){
        NepFriendshipApplication application = friendshipApplicationMapper.queryFriendshipApplicationByUserId(1, 18, 19);
        log.info("application: {}", application);
    }

    @Test
    public void queryAllFriendshipApplication(){
        List<NepFriendshipApplication> applicationList = friendshipApplicationMapper.queryAllFriendshipApplication(1, 18);
        for (NepFriendshipApplication application : applicationList) {
            log.info("application: {}", application);
        }
    }

}
