package com.fuyusakaiori.nep.im.service.core.user.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.NepCancelUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.NepEditUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.NepQueryUserByAccountRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.NepRegisterUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepModifyUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class NepUserServiceTest
{

    @Autowired
    private INepUserService userService;

    @Test
    public void registerTest(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepRegisterUser user = new NepRegisterUser()
                                       .setUserNickName(RandomUtil.randomString(10))
                                       .setUserPassword("123")
                                       .setUserGender(RandomUtil.randomInt(3));
        NepRegisterUserRequest request = new NepRegisterUserRequest()
                                                 .setRequestHeader(header).setRequestBody(user);
        NepModifyUserResponse response = userService.registerUser(request);
        log.info("response: {}", response);
    }

    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepEditUser user = new NepEditUser().setUserId(15).setUserNickName("670232228").setUserGender(2)
                                       .setUserBirthday(DateUtil.parse("2001-03-02").getTime())
                                       .setUserAge(18).setUserLocation("中国");
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
                                               .setUserId(1);
        // 2. 执行删除
        NepModifyUserResponse response = userService.cancelUser(request);
        // 3. 输出结果
        log.info("response: {}", response);
    }


}
