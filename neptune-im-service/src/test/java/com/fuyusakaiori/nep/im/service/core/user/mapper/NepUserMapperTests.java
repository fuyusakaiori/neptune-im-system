package com.fuyusakaiori.nep.im.service.core.user.mapper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class NepUserMapperTests {

    @Autowired
    private INepUserMapper userMapper;

    private static final int BATCH_COUNT = 10;


    @Test
    public void insertUserTest(){
        NepRegisterUser user = new NepRegisterUser()
                                       .setUserNickName(RandomUtil.randomString(10))
                                       .setUserPassword("123")
                                       .setUserType(1)
                                       .setUserGender(RandomUtil.randomInt(3));
        int result = userMapper.registerUser(1, user, System.currentTimeMillis(), System.currentTimeMillis());
        log.info("插入成功的数据量: {}", result);
    }

    @Test
    public void batchInsertUserTest(){
        // 1. 准备批量插入的数据
        List<NepRegisterUser> userList = new ArrayList<>(BATCH_COUNT);
        // 2. 循环创建数据
        for (int index = 0; index < BATCH_COUNT; index++) {
            userList.add(new NepRegisterUser()
                                 .setUserNickName(RandomUtil.randomString(10))
                                 .setUserPassword("123")
                                 .setUserType(1)
                                 .setUserGender(RandomUtil.randomInt(3)));
        }
        // 3. 批量插入数据
        long start = System.currentTimeMillis();
        int result = userMapper.batchRegisterUser(1, userList, System.currentTimeMillis(), System.currentTimeMillis());
        long time = System.currentTimeMillis() - start;
        log.info("foreach 耗时: {}", time);
        // 4. 检测是否插入成功
        log.info("插入成功的数据量: {}", result);
    }

    @Test
    public void deleteUserTest(){
        // 1. 准备参数
        int userId = 2;
        // 2. 执行单次删除
        int result = userMapper.cancelUser(1, userId, System.currentTimeMillis());
        // 3. 检测结果
        log.info("删除的用户数: {}", result);
    }

    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NepEditUser user = new NepEditUser()
                                   .setUserId(4)
                                   .setUserNickName("我")
                                   .setUserGender(1)
                                   .setUserPassword("456")
                                   .setUserBirthday(DateUtil.parse("2001-02-03").getTime())
                                   .setUserAge(22)
                                   .setUserLocation("中国");
        // 2. 更新用户
        int result = userMapper.editUser(1, user, System.currentTimeMillis());
        // 3. 检查结果
        log.info("result: {}", result);
    }

    @Test
    public void selectSimpleUserTest(){
        NepUser user1 = userMapper.querySimpleUserById(1, 1);
        log.info("user1: {}", user1);
        NepUser user2 = userMapper.queryDetailedUser(1, 1, null);
        log.info("user2: {}", user2);
        NepUser user3 = userMapper.querySimpleUserByNickName(1, "冬坂五百里");
        log.info("user3: {}", user3);

    }

}
