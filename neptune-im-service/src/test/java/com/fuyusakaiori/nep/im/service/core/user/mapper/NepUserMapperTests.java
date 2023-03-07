package com.fuyusakaiori.nep.im.service.core.user.mapper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
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

    private static final int BATCH_COUNT = 5;


    /**
     * <h3>测试注册用户</h3>
     */
    @Test
    public void registerUserTest(){
        NepRegisterUser user = new NepRegisterUser()
                                       .setUserAccount(RandomUtil.randomNumbers(9))
                                       .setUserNickName(RandomUtil.randomString(10))
                                       .setUserPassword("123");
        int result = userMapper.registerUser(1, user, System.currentTimeMillis(), System.currentTimeMillis());
        log.info("插入成功的数据量: {}", result);
    }

    /**
     * <h3>测试批量注册用户</h3>
     */
    @Test
    public void batchInsertUserTest(){
        // 1. 准备批量插入的数据
        List<NepRegisterUser> userList = new ArrayList<>(BATCH_COUNT);
        // 2. 循环创建数据
        for (int index = 0; index < BATCH_COUNT; index++) {
            userList.add(new NepRegisterUser()
                                 .setUserAccount(RandomUtil.randomNumbers(10))
                                 .setUserNickName(RandomUtil.randomString(10))
                                 .setUserPassword("123"));
        }
        // 3. 批量插入数据
        int result = userMapper.batchRegisterUser(1, userList, System.currentTimeMillis(), System.currentTimeMillis());
        // 4. 检测是否插入成功
        log.info("插入成功的数据量: {}", result);
    }

    /**
     * <h3>测试删除用户</h3>
     */
    @Test
    public void deleteUserTest(){
        // 1. 准备参数
        int userId = 21;
        // 2. 执行单次删除
        int result = userMapper.cancelUser(1, userId, System.currentTimeMillis());
        // 3. 检测结果
        log.info("删除的用户数: {}", result);
    }

    /**
     * <h3>测试更新用户</h3>
     */
    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NepEditUser user = new NepEditUser()
                                   .setUserId(5)
                                   .setUserNickName("无名氏")
                                   .setUserGender(1)
                                   .setUserPassword("456")
                                   .setUserBirthday(DateUtil.parse("2004-09-03").getTime())
                                   .setUserAge(14)
                                   .setUserSelfSignature("笨蛋如我也要放声歌唱")
                                   .setUserLocation("未知");
        // 2. 更新用户
        int result = userMapper.editUser(1, user, System.currentTimeMillis());
        // 3. 检查结果
        log.info("result: {}", result);
    }

}
