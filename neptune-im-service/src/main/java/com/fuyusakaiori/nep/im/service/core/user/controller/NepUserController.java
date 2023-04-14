package com.fuyusakaiori.nep.im.service.core.user.controller;


import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.service.INepUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/nep/user")
public class NepUserController {

    @Autowired
    private INepUserService userService;

    @PostMapping(value = "/register")
    public NepRegisterUserResponse registerUser(@RequestBody NepRegisterUserRequest request){
        log.info("NepUserController registerUser: 准备开始注册用户 - request: {}", request);
        NepRegisterUserResponse response = userService.registerUser(request);
        log.info("NepUserController registerUser: 注册用户结束 - request: {}, response: {}", request, response);
        return response;
    }


    @PostMapping(value = "/login")
    public NepLoginUserResponse loginUser(@RequestBody NepLoginUserRequest request){
        log.info("NepUserController loginUser: 准备开始登陆用户 - request: {}", request);
        NepLoginUserResponse response = userService.loginUserInImSystem(request);
        log.info("NepUserController loginUser: 用户登录结束 - request: {}, response: {}", request, response);
        return response;
    }


    @CrossOrigin
    @PostMapping(value = "/upload-avatar")
    public NepEditUserAvatarResponse editUserAvatar(@RequestBody NepEditUserAvatarRequest request){
        log.info("NepUserController editUserAvatar: 开始更新用户头像 - request: {}", request);
        NepEditUserAvatarResponse response = userService.updateUserAvatar(request);
        log.info("NepUserController editUserAvatar: 用户头像更新结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/update-user-info")
    public NepEditUserInfoResponse editUserInfo(@RequestBody NepEditUserInfoRequest request){
        log.info("NepUserController editUserInfo: 开始更新用户资料 - request: {}", request);
        NepEditUserInfoResponse response = userService.updateUserInfo(request);
        log.info("NepUserController editUserInfo: 用户资料更新结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query")
    public NepQueryWillBeFriendByIdResponse queryWillBeFriendById(@RequestParam("appId") int appId, @RequestParam("clientType") int clientType, @RequestParam("imei") String imei,
                                                                  @RequestParam("userId") int userId){
        NepRequestHeader header = new NepRequestHeader().setAppId(appId)
                                          .setClientType(clientType).setImei(imei);
        NepQueryWillBeFriendByIdRequest request = new NepQueryWillBeFriendByIdRequest()
                                                          .setHeader(header).setUserId(userId);
        log.info("NepUserController queryWillBeFriendById: 开始根据用户 ID 查询用户 - request: {}", request);
        NepQueryWillBeFriendByIdResponse response = userService.queryWillBeFriendById(request);
        log.info("NepUserController queryWillBeFriendById: 根据用户 ID 查询用户结束 - request: {}, response: {}", request, response);
        return response;
    }


    @GetMapping(value = "/search-user")
    public NepQueryWillBeFriendResponse queryWillBeFriend(@RequestParam("appId") Integer appId, @RequestParam("userId") Integer userId, @RequestParam("username") String username, @RequestParam("nickname") String nickname){
        NepQueryWillBeFriendRequest request = new NepQueryWillBeFriendRequest()
                                              .setUserId(userId)
                                              .setUsername(username)
                                              .setNickname(nickname)
                                              .setHeader(new NepRequestHeader().setAppId(appId));
        log.info("NepUserController queryWillBeFriend: 开始根据用户昵称和账号查询用户 - request: {}", request);
        NepQueryWillBeFriendResponse response = userService.queryWillBeFriend(request);
        log.info("NepUserController queryWillBeFriend: 根据用户昵称和账号查询用户结束 - request: {}, response: {}", request, response);
        return response;
    }


}
