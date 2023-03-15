package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepCallBackConstant;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.config.NepApplicationConfig;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.util.callback.INepCallBackService;
import com.fuyusakaiori.nep.im.service.util.fdfs.NepFastDFSClient;
import com.fuyusakaiori.nep.im.service.util.file.NepFileUtils;
import com.fuyusakaiori.nep.im.service.util.file.NepPathUtil;
import com.fuyusakaiori.nep.im.service.util.mq.publish.NepServiceToGateWayMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NepUserServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private NepApplicationConfig applicationConfig;

    @Autowired
    private INepCallBackService callBackService;

    @Autowired
    private NepServiceToGateWayMessageProducer messageProducer;

    @Autowired
    private NepFastDFSClient fastDFSClient;

    public int doRegisterUser(NepRegisterUserRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        String username = request.getUsername();
        // 2. 查询用户
        NepUser queryUser = userMapper.queryUserByUserName(appId, username);
        // 3. 校验用户是否存在
        if (Objects.nonNull(queryUser) && !queryUser.isDelete()){
            log.error("NepUserServiceImpl doRegisterUser: 用户名已经存在! - request: {}", request);
            return 0;
        }
        // 4. 封装注册信息
        NepUser registerUser = BeanUtil.copyProperties(request, NepUser.class, "header");
        // 5. 注册用户
        return userMapper.registerUser(appId, registerUser);
    }

    public NepUser doUpdateUserInfo(NepEditUserInfoRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer appId = header.getAppId();
        Integer userId = request.getUserId();
        // 2. 查询用户
        NepUser queryUser = userMapper.queryUserById(appId, userId);
        // 3. 校验用户是否存在
        if (Objects.isNull(queryUser) || queryUser.isDelete()){
            log.error("NeptuneUserService doUpdateUserInfo: 更新资料的用户不存在 - request: {}", request);
            return null;
        }
        // 4. 将请求中携带的内容拷贝到实体对象中
        NepUser updateUser = BeanUtil.copyProperties(request, NepUser.class, "header");
        // 5. 更新用户资料
        int result = userMapper.updateUserInfo(appId, updateUser);
        if (result >= 0){
            return userMapper.queryUserById(appId, userId);
        }
        // TODO 6. 发送消息通知其他客户端

        // TODO 7. 更新用户资料后执行回调: 回调是交给第三方业务服务器处理的, 即时通信系统不会处理这个, 这里只是做个样子而已
        return null;
    }

    public NepUser doUpdateUserAvatarAddress(NepEditUserAvatarRequest request){
        // 1. 获取变量
        Integer userId = request.getUserId();
        String avatarAddress = request.getAvatarAddress();
        Integer appId = request.getHeader().getAppId();
        // 2. 查询用户
        NepUser queryUser = userMapper.queryUserById(appId, userId);
        // 3. 校验用户是否存在
        if (Objects.isNull(queryUser) || queryUser.isDelete()){
            log.error("NeptuneUserService doUpdateUserAvatarAddress: 更新头像的用户不存在 - request: {}", request);
            return null;
        }
        // 4. 头像地址转换为文件对象
        String filePath = NepPathUtil.getImageBasePath() + NepPathUtil.getUserImagePath(userId);
        boolean isToFile = NepFileUtils.base64ToFile(filePath, avatarAddress);
        // 5. 检验转换是否成功
        if (isToFile){
            log.error("NeptuneUserService doUpdateUserAvatarAddress: 根据图片地址转换文件失败 - request: {}, filePath: {}", request, filePath);
            return null;
        }
        // 6. 转换为文件上传对象
        MultipartFile avatarFile = NepFileUtils.fileToMultipart(filePath);
        // 7. 检验转换是否成功
        if (Objects.isNull(avatarFile)){
            log.error("NeptuneUserService doUpdateUserAvatarAddress: 根据文件地址转换为文件上传对象失败 - request: {}, filePath: {}", request, filePath);
            return null;
        }
        // 7. 上传到分布式文件服务器
        String uploadAvatarAddress = fastDFSClient.uploadBase64(avatarFile);
        // 8. 校验是否上传成功
        if (StrUtil.isEmpty(uploadAvatarAddress)){
            log.error("NeptuneUserService doUpdateUserAvatarAddress: 文件对象上传到分布式服务器失败 - request: {}, filePath: {}", request, filePath);
            return null;
        }
        // 9. 更新用户头像地址
        int result = userMapper.updateUserAvatarAddress(appId, userId, uploadAvatarAddress, System.currentTimeMillis());
        if (result > 0){
            // 10. 重新查询新的结果
            return userMapper.queryUserById(appId, userId);
        }
        // TODO 11. 发送消息通知其他客户端

        // TODO 12. 执行回调
        return null;
    }


    public int doCancelUser(NepCancelUserRequest request){
        int result = 0;
        // 1. 获取变量
        Integer userId = request.getUserId();
        Integer appId = request.getHeader().getAppId();
        // 2. 查询用户
        NepUser queryUser = userMapper.queryUserById(appId, userId);
        // 3. 校验用户是否存在
        if (Objects.isNull(queryUser) || queryUser.isDelete()){
            log.error("NeptuneUserService cancelUser: 需要删除的用户不存在 - request: {}", request);
            return result;
        }
        // 4. 删除用户
        return userMapper.cancelUser(appId, userId, System.currentTimeMillis());
    }


    public List<NepUser> doQueryUserByNickName(NepQueryUserByNickNameRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        String nickname = request.getUserNickName();
        // 3. 查询结果
        List<NepUser> userList = userMapper.queryUserByNickName(header.getAppId(), nickname);
        // 4. 检查结果
        if (CollectionUtil.isEmpty(userList)){
            log.info("NeptuneUserService doQueryUserByNickName: 没有查询到任何用户 - request: {}", request);
            return Collections.emptyList();
        }
        return userList;
    }

    public NepUser doQueryUserByUserName(NepQueryUserByUserNameRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        String username = request.getUsername();
        // 2. 查询结果
        NepUser user = userMapper.queryUserByUserName(header.getAppId(), username);
        // 3. 检查结果
        if (Objects.isNull(user) || user.isDelete()){
            log.info("NeptuneUserService doQueryUserByUserName: 没有查询到相应的用户 - request: {}", request);
            return null;
        }
        return user;
    }

    public NepUser doLoginUserInImSystem(NepLoginUserRequest request) {
        // 1. 获取变量
        String username = request.getUsername();
        String password = request.getPassword();
        Integer appId = request.getHeader().getAppId();
        // 2. 查询用户
        NepUser user = userMapper.loginUserInImSystem(appId, username, password);
        // 3. 校验用户
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NeptuneUserService doQueryUserByUserName: 没有查询到相应的用户 - request: {}", request);
            return null;
        }
        return user;
    }
}
