package com.fuyusakaiori.nep.im.service.core.user.service;


import cn.hutool.core.date.DateUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepCancelUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepQueryUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepModifyUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.NepQueryUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@Slf4j
@SpringBootTest
public class NepUserServiceTest
{

    @Autowired
    private INepUserService userService;

    @Test
    public void registerTest(){

    }

    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepEditUser user = new NepEditUser().setUserId(13).setUserNickName("喜多郁代").setUserGender(2)
                                       .setUserBirthday(DateUtil.parse("2005-04-21").getTime())
                                       .setUserAge(18).setUserLocation("日本");
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
        NepCancelUser user = new NepCancelUser().setUserId(12).setUserNickName("");
        NepCancelUserRequest request = new NepCancelUserRequest().setRequestHeader(header).setRequestBody(user);
        // 2. 执行删除
        NepModifyUserResponse response = userService.cancelUser(request);
        // 3. 输出结果
        log.info("response: {}", response);
    }

    @Test
    public void queryDetailedUserByIdTest(){
        NepRequestHeader header = new NepRequestHeader().setAppId(1);
        NepQueryUserByIdRequest request = new NepQueryUserByIdRequest()
                                                  .setRequestHeader(header).setUserId(1);
        NepQueryUserResponse response = userService.queryDetailedUserById(request);
        log.info("response: {}", response);
    }

    @Test
    public void queryFriendlyUserByIdListTest(){
        NepQueryUserByIdListRequest request = new NepQueryUserByIdListRequest()
                                                      .setRequestHeader(new NepRequestHeader().setAppId(1))
                                                      .setRequestBody(Arrays.asList(1, 2, 3));
        NepQueryUserResponse response = userService.queryFriendlyUserByIdList(request);
        log.info("response: {}", response);
    }


}
