package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepEditFriendship;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class NepFriendshipMapperTest {

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Test
    public void addFriendshipTest(){
        NepAddFriendship friendship = new NepAddFriendship()
                                                    .setFriendFromId(4)
                                                    .setFriendToId(5)
                                                    .setFriendshipSource("网络搜索")
                                                    .setFriendRemark("しろこ")
                                                    .setAdditionalInfo("我是斧乃木余接");
        int result = friendshipMapper.addFriendship(1, friendship, System.currentTimeMillis(), System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void editFriendshipTest(){
        NepEditFriendship friendship = new NepEditFriendship()
                                               .setFriendFromId(1)
                                               .setFriendToId(2)
                                               .setFriendRemark("波奇酱")
                                               .setFriendshipStatus(0);
        int result = friendshipMapper.editFriendship(1, friendship, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void releaseFriendshipTest(){
        int result = friendshipMapper.releaseFriendship(1, 1, 6, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void releaseAllFriendshipTest(){
        int result = friendshipMapper.releaseAllFriendship(1, 1, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void queryFriendshipByIdTest(){
        NepFriendship friendship = friendshipMapper.queryFriendshipById(1, 5, 4);
        log.info("friendship: {}", friendship);
    }

    @Test
    public void queryFriendshipByIdListTest(){
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByIdList(1, 1, Arrays.asList(2, 3));
        for (NepFriendship friendship : friendshipList) {
            log.info("friendship: {}", friendship);
        }
    }

    @Test
    public void queryFriendshipByRemarkTest(){
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByRemark(1, 1, "小");
        for (NepFriendship friendship : friendshipList) {
            log.info("friendship: {}", friendship);
        }
    }

    @Test
    public void queryAllFriendshipTest(){
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(1, 1);
        for (NepFriendship friendship : friendshipList) {
            log.info("friendship: {}", friendship);
        }
    }

}
