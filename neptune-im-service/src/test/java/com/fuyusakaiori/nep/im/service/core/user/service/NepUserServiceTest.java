package com.fuyusakaiori.nep.im.service.core.user.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepModifyUserResponse;
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
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepRegisterUser user = new NepRegisterUser()
                                       .setUserAccount(RandomUtil.randomNumbers(12))
                                       .setUserNickName(RandomUtil.randomString(10))
                                       .setUserPassword("123");
        NepRegisterUserRequest request = new NepRegisterUserRequest()
                                                 .setRequestHeader(header).setRequestBody(user);
        NepModifyUserResponse response = userService.registerUser(request);
        log.info("response: {}", response);
    }

    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepEditUser user = new NepEditUser().setUserId(30).setUserNickName("幼儿缘").setUserGender(2)
                                       .setUserBirthday(DateUtil.parse("2010-10-01").getTime())
                                       .setUserSelfSignature("地球是原的")
                                       .setUserAvatarAddress("unknown")
                                       .setUserAge(18).setUserLocation("美国");
        NepEditUserRequest request = new NepEditUserRequest().setRequestBody(user).setRequestHeader(header);
        // 2. 执行更新
        NepModifyUserResponse response = userService.updateUser(request);
        // 3. 输出结果
        log.info("response: {}", response);
    }

    @Test
    public void cancelUserTest(){
        // 1. 准备参数
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepCancelUserRequest request = new NepCancelUserRequest().setRequestHeader(header)
                                               .setUserId(24);
        // 2. 执行删除
        NepModifyUserResponse response = userService.cancelUser(request);
        // 3. 输出结果
        log.info("response: {}", response);
    }


    @Test
    public void queryUserByAccountTest(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepQueryUserByAccountRequest request = new NepQueryUserByAccountRequest()
                                                       .setRequestHeader(header)
                                                       .setUserAccount("");
        NepQueryUserResponse response = userService.queryUserByAccount(request);
        log.info("response: {}", response);
    }

    @Test
    public void queryUserByNickNameTest(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepQueryUserByNickNameRequest request = new NepQueryUserByNickNameRequest()
                                                       .setRequestHeader(header)
                                                       .setUserNickName("我超");
        NepQueryUserResponse response = userService.queryUserByNickName(request);
        for (NepUser user : response.getUserList()) {
            log.info("user: {}", user);
        }
    }


}
