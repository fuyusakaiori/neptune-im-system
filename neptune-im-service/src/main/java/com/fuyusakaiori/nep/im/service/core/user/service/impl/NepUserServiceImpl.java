package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.config.NepApplicationConfig;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepWillBeFriend;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.util.callback.INepCallBackService;
import com.fuyusakaiori.nep.im.service.util.fdfs.NepFastDFSClient;
import com.fuyusakaiori.nep.im.service.util.file.NepFileUtils;
import com.fuyusakaiori.nep.im.service.util.file.NepPathUtil;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepUserServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

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

    public List<NepWillBeFriend> doQueryWillBeFriend(NepQueryWillBeFriendRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        String username = request.getUsername();
        String nickname = request.getNickname();
        // 2. 根据用户名查询用户
        NepUser user = userMapper.queryUserByUserName(appId, username);
        // 3. 根据昵称查询用户
        List<NepUser> userList = userMapper.queryUserByNickName(appId, nickname);
        // 4. 检查结果
        if ((Objects.isNull(user) || user.isDelete()) && CollectionUtil.isEmpty(userList)){
            log.info("NeptuneUserService doQueryWillBeFriend: 没有查询到相应的用户 - request: {}", request);
            return Collections.emptyList();
        }
        // 5. 合并结果
        List<NepUser> currentUserList = new ArrayList<>();
        if (Objects.nonNull(user)){
            currentUserList.add(user);
        }
        if (CollectionUtil.isNotEmpty(userList)) {
            currentUserList.addAll(userList);
        }
        // 6. 结果去重
        Map<Integer, NepUser> userMap = currentUserList.stream().collect(Collectors.toMap(NepUser::getUserId, NepUser -> NepUser));
        // 7. 判断查询得到的用户是否是好友
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByIdList(appId, userId, new ArrayList<>(userMap.keySet()));
        // 8. 校验好友关系是否为空, 如果为空的话, 直接把用户信息拷贝到新的实体中
        if (CollectionUtil.isEmpty(friendshipList)){
             return BeanUtil.copyToList(userMap.values(), NepWillBeFriend.class);
        }
        // 9. 拼装返回结果
        List<NepWillBeFriend> willBeFriendList = new ArrayList<>();
        for (NepFriendship friendship : friendshipList) {
            willBeFriendList.add(BeanUtil.copyProperties(userMap.get(friendship.getFriendToId()), NepWillBeFriend.class));
        }
        return willBeFriendList;
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
