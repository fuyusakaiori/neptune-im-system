package com.fuyusakaiori.nep.im.service.core.user.mapper;


import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class NepFriendUserMapperTest {

    @Autowired
    private INepFriendUserMapper friendUserMapper;

    @Test
    public void queryFriendUserByIdTest(){
        NepUser user = friendUserMapper.queryFriendUserById(1, 1);
        log.info("user: {}", user);
    }

    @Test
    public void queryFriendUserByNickNameTest(){
        List<NepUser> userList = friendUserMapper.queryFriendUserByNickName(1, "喜多");
        for (NepUser user : userList) {
            log.info("user{}: {}",user.getUserId(), user);
        }
    }

    @Test
    public void queryFriendUserByIdListTest(){
        List<NepUser> userList = friendUserMapper.queryFriendUserByIdList(1, Arrays.asList(1, 2, 3, 4));
        for (NepUser user : userList) {
            log.info("user{}: {}",user.getUserId(), user);
        }
    }

}
