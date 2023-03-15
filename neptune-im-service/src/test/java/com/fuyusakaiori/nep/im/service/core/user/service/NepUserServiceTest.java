package com.fuyusakaiori.nep.im.service.core.user.service;


import cn.hutool.core.util.RandomUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepRegisterUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepQueryUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class NepUserServiceTest {
    @Autowired
    private INepUserService userService;

    @Test
    public void registerTest(){

    }

    @Test
    public void updateUserTest(){

    }

    @Test
    public void cancelUserTest(){

    }


    @Test
    public void queryUserByAccountTest(){

    }

    @Test
    public void queryUserByNickNameTest(){

    }


}
