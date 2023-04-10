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

    /**
     * TODO 查询的结果需要包含是否是好友
     */
    @GetMapping(value = "/search-user")
    public NepQueryWillBeFriendResponse queryUser(@RequestParam("appId") Integer appId, @RequestParam("userId") Integer userId, @RequestParam("username") String username, @RequestParam("nickname") String nickname){
        NepQueryWillBeFriendRequest request = new NepQueryWillBeFriendRequest()
                                              .setUserId(userId)
                                              .setUsername(username)
                                              .setNickname(nickname)
                                              .setHeader(new NepRequestHeader().setAppId(appId));
        log.info("NepUserController queryUserByUserName: 开始查询用户 - request: {}", request);
        NepQueryWillBeFriendResponse response = userService.queryWillBeFriend(request);
        log.info("NepUserController queryUserByUserName: 查询用户结束 - request: {}, response: {}", request, response);
        return response;
    }


}
