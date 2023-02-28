package com.fuyusakaiori.neptune.im.service.core.user.service;


import cn.hutool.core.date.DateUtil;
import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneCancelUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneEditUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneQueryUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneCancelUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneEditUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneQueryUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.response.NeptuneModifyUserResponse;
import com.fuyusakaiori.neptune.im.service.core.user.entity.response.NeptuneQueryUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class NeptuneUserServiceTest {

    @Autowired
    private INeptuneUserService userService;

    @Test
    public void registerTest(){

    }

    @Test
    public void updateUserTest(){
        // 1. 准备参数
        NeptuneRequestHeader header = new NeptuneRequestHeader().setAppId(1);
        NeptuneEditUser user = new NeptuneEditUser().setUserId(13).setUserNickName("喜多郁代").setUserGender(2)
                                       .setUserBirthday(DateUtil.parse("2005-04-21").getTime())
                                       .setUserAge(18).setUserLocation("日本").setUpdateTime(System.currentTimeMillis());
        NeptuneEditUserRequest request = new NeptuneEditUserRequest().setRequestBody(user).setRequestHeader(header);
        // 2. 执行更新
        NeptuneModifyUserResponse response = userService.updateUser(request);
        // 3. 输出结果
        log.info("response: {}", response);
    }

    @Test
    public void cancelUserTest(){
        // 1. 准备参数
        NeptuneRequestHeader header = new NeptuneRequestHeader().setAppId(1);
        List<NeptuneCancelUser> users = new ArrayList<>(10);
        for (int index = 0; index < 1; index++) {
            users.add(new NeptuneCancelUser().setUserId(12));
        }
        NeptuneCancelUserRequest request = new NeptuneCancelUserRequest()
                                                   .setRequestHeader(header).setRequestBody(users);
        // 2. 执行删除
        NeptuneModifyUserResponse response = userService.cancelUser(request);
        // 3. 输出结果
        log.info("response: {}", response);
    }

    @Test
    public void queryUserTest(){
        // 1. 准备参数
        NeptuneRequestHeader header = new NeptuneRequestHeader().setAppId(2);
        NeptuneQueryUser user = new NeptuneQueryUser().setIsDelete(false)
                                        .setUserGender(1).setUserType(1).setIsForbid(false);
        NeptuneQueryUserRequest request = new NeptuneQueryUserRequest()
                                                  .setRequestHeader(header).setRequestBody(user);
        // 2. 执行查询
        NeptuneQueryUserResponse response = userService.queryUser(request);
        // 3. 输出结果
        log.info("response: {}", response);
    }


}
