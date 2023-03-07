package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@Slf4j
@SpringBootTest
public class NepFriendshipGroupMemberMapperTest {

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;


    @Test
    public void addFriendshipGroupMemberTest(){
        int result = friendshipGroupMemberMapper.addFriendshipGroupMember(1, 2, Arrays.asList(2, 3, 4, 5, 6),
                System.currentTimeMillis(), System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void moveFriendshipGroupMemberTest(){
        int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(1, 4, Arrays.asList(2, 3), System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void clearFriendshipGroupMemberTest(){
        int result = friendshipGroupMemberMapper.clearFriendshipGroupMember(1, 2, System.currentTimeMillis());
        log.info("result: {}", result);
    }

    @Test
    public void queryFriendshipGroupMemberByMemberIdListTest(){
        List<Integer> joinedMemberIdList = friendshipGroupMemberMapper.queryFriendshipGroupMemberByMemberIdList(1, Arrays.asList(2, 3, 7, 8));
        log.info("joined: {}", joinedMemberIdList);
    }

    @Test
    public void queryAllFriendshipGroupMemberTest(){
        List<NepFriendshipGroupMember> friendshipGroupMemberList = friendshipGroupMemberMapper.queryAllFriendshipGroupMember(1, Arrays.asList(2, 3, 4));
        Map<Integer, List<Integer>> groupIdAndGroupMemberIdList = new HashMap<>();
        for (NepFriendshipGroupMember friendshipGroupMember : friendshipGroupMemberList) {
            List<Integer> groupMemberIdList = groupIdAndGroupMemberIdList.getOrDefault(friendshipGroupMember.getFriendshipGroupId(), new ArrayList<>());
            groupMemberIdList.add(friendshipGroupMember.getFriendshipGroupMemberId());
            groupIdAndGroupMemberIdList.put(friendshipGroupMember.getFriendshipGroupId(), groupMemberIdList);
        }
        log.info("map: {}", groupIdAndGroupMemberIdList);
    }


}
