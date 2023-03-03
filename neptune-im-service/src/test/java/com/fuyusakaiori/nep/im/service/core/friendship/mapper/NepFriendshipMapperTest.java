package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepEditFriendship;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class NepFriendshipMapperTest {

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Test
    public void addFriendship(){
        NepAddFriendship friendship = new NepAddFriendship()
                                                    .setFriendFromId(1)
                                                    .setFriendToId(3)
                                                    .setFriendshipSource("系统申请")
                                                    .setFriendRemark("小喜多")
                                                    .setAdditionalInfo("好友申请");
        int result = friendshipMapper.addFriendship(1, friendship, System.currentTimeMillis(), System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void editFriendship(){
        NepEditFriendship friendship = new NepEditFriendship()
                                               .setFriendFromId(1)
                                               .setFriendToId(2)
                                               .setFriendRemark("波奇酱")
                                               .setFriendshipStatus(1);
        int result = friendshipMapper.editFriendship(1, friendship, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void releaseFriendship(){
        int result = friendshipMapper.releaseFriendship(1, 1, 2, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void releaseAllFriendship(){
        int result = friendshipMapper.releaseAllFriendship(1, 1, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void queryFriendshipById(){
        NepFriendship friendship = friendshipMapper.queryFriendshipById(1, 1, 2);
        log.info("friendship: {}", friendship);
    }

    @Test
    public void queryFriendshipByRemark(){
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByRemark(1, 1, "小");
        for (NepFriendship friendship : friendshipList) {
            log.info("friendship: {}", friendship);
        }
    }

    @Test
    public void queryAllFriendship(){
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(1, 1);
        for (NepFriendship friendship : friendshipList) {
            log.info("friendship: {}", friendship);
        }
    }

}
