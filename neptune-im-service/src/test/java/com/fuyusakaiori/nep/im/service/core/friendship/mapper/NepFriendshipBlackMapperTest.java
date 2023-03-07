package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class NepFriendshipBlackMapperTest {

    @Autowired
    private INepFriendshipBlackMapper friendshipBlackMapper;

    @Test
    public void addFriendInBlackListTest(){
        int result = friendshipBlackMapper.addFriendInBlackList(1, 1, 2, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void removeFriendInBlackListTest(){
        int result = friendshipBlackMapper.removeFriendInBlackList(1, 1, 2, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void checkFriendInBlackListTest(){
        int result = friendshipBlackMapper.checkFriendInBlackList(1, 1, 2);
        log.info("result: {}", result);
    }

    @Test
    public void checkBiFriendInBlackListTest(){
        int result = friendshipBlackMapper.checkBiFriendInBlackList(1, 1, 2);
        log.info("result: {}", result);
    }

    @Test
    public void queryAllFriendInBlackListTest(){
        List<Integer> result = friendshipBlackMapper.queryAllFriendInBlackList(1, 1);
        log.info("result: {}", result);
    }

}
