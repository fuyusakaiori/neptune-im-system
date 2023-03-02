package com.fuyusakaiori.nep.im.service.core.user.mapper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepCancelUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepQueryUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class NepUserMapperTests
{

    @Autowired
    private INepUserMapper userMapper;

    private static final int BATCH_COUNT = 10;

    @Test
    public void insertUserTest(){
        // 1. 准备批量插入的数据
        List<NepRegisterUser> userList = new ArrayList<>(BATCH_COUNT);
        // 2. 循环创建数据
        for (int index = 0; index < BATCH_COUNT; index++) {
            userList.add(new NepRegisterUser()
                                 .setUserNickName(RandomUtil.randomString(10))
                                 .setUserPassword("123")
                                 .setUserGender(RandomUtil.randomInt(3)));
        }
        // 3. 批量插入数据
        long start = System.currentTimeMillis();
        int result = userMapper.batchRegisterUser(1, userList);
        long time = System.currentTimeMillis() - start;
        log.info("foreach 耗时: {}", time);
        // 4. 检测是否插入成功
        log.info("插入成功的数据量: {}", result);
        // 5. 测试的时候得到结果: 如果数据量低于 1000, 那么 mybatis plus 封装的 API 是非常快的; 如果数据量高于 1000, 那么 foreach 是非常稳定的, 不会因为数据量变化而耗时增加
    }

    @Test
    public void deleteUserTest(){
        // 1. 准备参数
        NepCancelUser deleteUser = new NepCancelUser().setUserNickName("喜多郁代");
        // 2. 执行单次删除
        int result = userMapper.cancelUser(1, deleteUser, System.currentTimeMillis());
        // 3. 检测结果
        log.info("删除的用户数: {}", result);
    }

    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NepEditUser user = new NepEditUser().setUserId(2).setUserNickName("吉他英雄").setUserGender(2).setUserPassword("456")
                                                  .setUserBirthday(DateUtil.parse("2005-02-21").getTime())
                                                  .setUserAge(18).setUserLocation("日本");
        // 2. 更新用户
        int result = userMapper.editUser(1, user, System.currentTimeMillis());
        // 3. 检查结果
        log.info("result: {}", result);
    }

    @Test
    public void selectUserTest(){
        // 1. 准备参数
        NepQueryUser user = new NepQueryUser().setIsDelete(false)
                                        .setUserGender(1).setUserType(1).setIsForbid(false);
        // 2. 查询用户
        List<NepUser> userList = userMapper.querySimpleUserByMultiCondition(1, user);
        // 3. 输出结果
        for (NepUser userInfo : userList) {
            log.info("用户信息: {}", userInfo);
        }
        // 4. 准备参数
        NepQueryUser guitar = new NepQueryUser().setUserNickName("吉他");
        // 5. 查询用户
        List<NepUser> guitars = userMapper.querySimpleUserByMultiCondition(1, guitar);
        // 6. 输出结果
        for (NepUser userInfo : guitars) {
            log.info("用户信息: {}", userInfo);
        }
    }

    @Test
    public void selectSimpleUserTest(){
        NepUser user1 = userMapper.querySimpleUserById(1, 1);
        log.info("user1: {}", user1);
        NepUser user2 = userMapper.queryDetailedUserById(1, 1);
        log.info("user2: {}", user2);
        NepUser user3 = userMapper.querySimpleUserByNickName(1, "冬坂五百里");
        log.info("user3: {}", user3);
        List<NepUser> userList = userMapper.queryFriendlyUserByIdList(1, Arrays.asList(1, 2, 3));
        log.info("users: {}", userList);

    }

}
