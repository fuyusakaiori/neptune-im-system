package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class NepFriendshipGroupMapperTest {

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Test
    public void createFriendshipGroupTest(){

    }


    @Test
    public void deleteFriendshipGroupTest(){
        int result = friendshipGroupMapper.deleteFriendshipGroup(1, 1, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void queryFriendshipGroupByIdTest(){
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(1, 2);
        log.info("friendshipGroup: {}", friendshipGroup);
    }

    @Test
    public void queryAllFriendshipGroupTest(){
        List<NepFriendshipGroup> friendshipGroupList = friendshipGroupMapper.queryAllFriendshipGroup(1, 1);
        for (NepFriendshipGroup friendshipGroup : friendshipGroupList) {
            log.info("friendshipGroup: {}", friendshipGroup);
        }
    }

}
