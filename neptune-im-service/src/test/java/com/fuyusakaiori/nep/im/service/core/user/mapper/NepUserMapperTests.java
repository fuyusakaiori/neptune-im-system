package com.fuyusakaiori.nep.im.service.core.user.mapper;

import cn.hutool.core.bean.BeanUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.NepRegisterUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    }

    /**
     * <h3>测试删除用户</h3>
     */
    @Test
    public void deleteUserTest(){

    }

    /**
     * <h3>测试更新用户</h3>
     */
    @Test
    public void updateUserTest(){

    }

}
