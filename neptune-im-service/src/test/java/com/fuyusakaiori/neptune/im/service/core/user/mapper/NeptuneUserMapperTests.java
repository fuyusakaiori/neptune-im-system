package com.fuyusakaiori.neptune.im.service.core.user.mapper;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fuyusakaiori.neptune.im.service.core.user.entity.NeptuneUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneCancelUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneEditUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneQueryUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneRegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class NeptuneUserMapperTests {

    @Autowired
    private INeptuneUserMapper userMapper;

    private static final int BATCH_COUNT = 10;

    @Test
    public void insertUserTest(){
        // 1. 准备批量插入的数据
        List<NeptuneRegisterUser> userList = new ArrayList<>(BATCH_COUNT);
        // 2. 循环创建数据
        for (int index = 0; index < BATCH_COUNT; index++) {
            userList.add(new NeptuneRegisterUser()
                                 .setUserNickName(RandomUtil.randomString(10))
                                 .setUserGender(RandomUtil.randomInt(3))
                                 .setCreateTime(System.currentTimeMillis())
                                 .setUpdateTime(System.currentTimeMillis()));
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
        NeptuneCancelUser deleteUser = new NeptuneCancelUser().setUserId(1);
        // 2. 执行单次删除
        int result = userMapper.cancelUser(1, deleteUser);
        // 3. 检测结果
        log.info("删除的用户数: {}", result);
        // 4. 准备参数
        List<NeptuneCancelUser> deleteUsers = new ArrayList<>();
        for (int index = 1; index < BATCH_COUNT; index++) {
            deleteUsers.add(new NeptuneCancelUser().setUserId(index + 1));
        }
        long start = System.currentTimeMillis();
        // 5. 执行批量删除
        result = userMapper.batchCancelUser(1, deleteUsers);
        long time = System.currentTimeMillis() - start;
        // 6. 检测结果
        log.info("花费时间: {}", time);
    }

    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NeptuneEditUser user = new NeptuneEditUser().setUserId(2).setUserNickName("吉他英雄").setUserGender(2)
                                                  .setUserBirthday(DateUtil.parse("2005-02-21").getTime())
                                                  .setUserAge(18).setUserLocation("日本").setUpdateTime(System.currentTimeMillis());
        // 2. 更新用户
        int result = userMapper.editUser(1, user);
        // 3. 检查结果
        log.info("result: {}", result);
    }

    @Test
    public void selectUserTest(){
        // 1. 准备参数
        NeptuneQueryUser user = new NeptuneQueryUser().setIsDelete(false)
                                        .setUserGender(1).setUserType(1).setIsForbid(false);
        // 2. 查询用户
        List<NeptuneUser> userList = userMapper.queryUser(1, user);
        // 3. 输出结果
        for (NeptuneUser userInfo : userList) {
            log.info("用户信息: {}", userInfo);
        }
        // 4. 准备参数
        NeptuneQueryUser guitar = new NeptuneQueryUser().setUserNickName("吉他");
        // 5. 查询用户
        List<NeptuneUser> guitars = userMapper.queryUser(1, guitar);
        // 6. 输出结果
        for (NeptuneUser userInfo : guitars) {
            log.info("用户信息: {}", userInfo);
        }
    }

}
