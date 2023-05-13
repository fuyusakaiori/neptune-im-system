package com.fuyusakaiori.nep.im.service.core.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatGroupMessageHeader;
import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatMessageBody;
import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatP2PMessageHeader;
import com.fuyusakaiori.nep.im.service.core.message.entity.dto.NepChatGroupHistoryMessage;
import com.fuyusakaiori.nep.im.service.core.message.entity.dto.NepChatP2PHistoryMessage;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatGroupHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.entity.request.NepLoadChatP2PHistoryMessageRequest;
import com.fuyusakaiori.nep.im.service.core.message.mapper.INepChatGroupMessageHeaderMapper;
import com.fuyusakaiori.nep.im.service.core.message.mapper.INepChatMessageBodyMapper;
import com.fuyusakaiori.nep.im.service.core.message.mapper.INepChatMessageHeaderMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepChatHistoryMessageServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepGroupMapper groupMapper;

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;

    @Autowired
    private INepChatMessageHeaderMapper chatMessageHeaderMapper;

    @Autowired
    private INepChatGroupMessageHeaderMapper chatGroupMessageHeaderMapper;

    @Autowired
    private INepChatMessageBodyMapper chatMessageBodyMapper;

    public List<NepChatP2PHistoryMessage> doLoadChatP2PHistoryMessage(NepLoadChatP2PHistoryMessageRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer senderId = request.getSenderId();
        Integer receiverId = request.getReceiverId();
        // 2. 查询消息发送者和消息接收者是否存在
        List<NepUser> userList = userMapper.queryUserByIdList(appId, Arrays.asList(senderId, receiverId));
        if (CollectionUtil.isEmpty(userList)){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatP2PHistoryMessage: 发送消息和接收消息的用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 3. 循环判断每个用户是否存在
        for (NepUser user : userList) {
            if (Objects.isNull(user) || user.isDelete()){
                log.error("NepChatHistoryMessageServiceImpl doLoadChatP2PHistoryMessage: 该用户不存在 - request: {}, user: {}", request, user);
                return Collections.emptyList();
            }
        }
        // 4. 查询历史聊天消息头
        List<NepChatP2PMessageHeader> messageHeaderList = chatMessageHeaderMapper.loadChatMessageHeader(appId, senderId, receiverId);
        if (CollectionUtil.isEmpty(messageHeaderList)){
            log.info("NepChatHistoryMessageServiceImpl doLoadChatP2PHistoryMessage: 没有查询任何聊天记录 - request: {}", request);
            return Collections.emptyList();
        }
        // 5. 查询历史聊天消息内容
        List<NepChatMessageBody> messageBodyList = chatMessageBodyMapper.loadChatMessageBody(appId,
                messageHeaderList.stream().map(NepChatP2PMessageHeader::getMessageKey).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(messageBodyList) || messageBodyList.size() != messageHeaderList.size()){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatP2PHistoryMessage: 历史聊天消息头和消息内容数量不对等 - request: {}", request);
            return Collections.emptyList();
        }
        // 6. 封装返回信息
        return transferP2PHistoryMessageList(messageHeaderList, messageBodyList);
    }

    private static List<NepChatP2PHistoryMessage> transferP2PHistoryMessageList(List<NepChatP2PMessageHeader> messageHeaderList, List<NepChatMessageBody> messageBodyList) {
        List<NepChatP2PHistoryMessage> messageList = new ArrayList<>();
        Map<Long, NepChatP2PMessageHeader> messageHeaderMap = messageHeaderList.stream().collect(
                Collectors.toMap(NepChatP2PMessageHeader::getMessageKey, NepChatP2PMessageHeader -> NepChatP2PMessageHeader));
        for (NepChatMessageBody messageBody : messageBodyList) {
            // 6.1 创建消息对象
            NepChatP2PHistoryMessage message = new NepChatP2PHistoryMessage();
            // 6.2 将消息内容拷贝到消息对象中
            BeanUtil.copyProperties(messageBody, message);
            // 6.3 将消息头中的内容拷贝到消息对象中
            NepChatP2PMessageHeader messageHeader = messageHeaderMap.get(messageBody.getMessageKey());
            BeanUtil.copyProperties(messageHeader, message);
            // 6.4 添加进结果中
            messageList.add(message);
        }
        return messageList;
    }

    public List<NepChatGroupHistoryMessage> doLoadChatGroupHistoryMessage(NepLoadChatGroupHistoryMessageRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer userId = request.getUserId();
        // 2. 查询用户是否存在
        NepUser user = userMapper.queryUserById(appId, userId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatGroupHistoryMessage: 用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 3. 查询群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatGroupHistoryMessage: 群聊不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 4. 查询用户是否在群聊中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (Objects.isNull(groupMember)
                    || Objects.nonNull(groupMember.getGroupMemberExitTime())
                    || Objects.nonNull(groupMember.getGroupMemberExitType())){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatGroupHistoryMessage: 用户不在群聊中, 不可以查看群聊历史消息 - request: {}", request);
            return Collections.emptyList();
        }
        // 5. 查询群聊历史消息头
        List<NepChatGroupMessageHeader> messageHeaderList = chatGroupMessageHeaderMapper.loadChatGroupMessageHeader(appId, groupId);
        if (CollectionUtil.isEmpty(messageHeaderList)){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatGroupHistoryMessage: 群聊中没有任何历史聊天记录 - request: {}", request);
            return Collections.emptyList();
        }
        // 6. 查询群聊历史消息内容
        List<NepChatMessageBody> messageBodyList = chatMessageBodyMapper.loadChatMessageBody(appId,
                messageHeaderList.stream().map(NepChatGroupMessageHeader::getMessageKey).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(messageBodyList) || messageBodyList.size() != messageHeaderList.size()){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatGroupHistoryMessage: 群聊的历史消息头和消息内容数量不一致 - request: {}", request);
            return Collections.emptyList();
        }
        // 7. 查询发送群聊消息的用户头像
        List<NepUser> userList = userMapper.queryUserByIdList(appId,
                messageHeaderList.stream().map(NepChatGroupMessageHeader::getMessageSenderId).distinct().collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(userList)){
            log.error("NepChatHistoryMessageServiceImpl doLoadChatGroupHistoryMessage: 发送群聊消息的用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 8. 封装返回信息
        return transferGroupHistoryMessageList(messageHeaderList, messageBodyList, userList);
    }

    private static List<NepChatGroupHistoryMessage> transferGroupHistoryMessageList(List<NepChatGroupMessageHeader> messageHeaderList, List<NepChatMessageBody> messageBodyList, List<NepUser> userList) {
        List<NepChatGroupHistoryMessage> messageList = new ArrayList<>();
        Map<Long, NepChatGroupMessageHeader> messageHeaderMap = messageHeaderList.stream().collect(
                Collectors.toMap(NepChatGroupMessageHeader::getMessageKey, NepChatGroupHistoryMessage -> NepChatGroupHistoryMessage));
        Map<Integer, NepUser> userMap = userList.stream().collect(
                Collectors.toMap(NepUser::getUserId, NepUser -> NepUser));
        for (NepChatMessageBody messageBody : messageBodyList) {
            NepChatGroupMessageHeader messageHeader = messageHeaderMap.get(messageBody.getMessageKey());
            NepUser user = userMap.get(messageHeader.getMessageSenderId());
            NepChatGroupHistoryMessage message = new NepChatGroupHistoryMessage();
            BeanUtil.copyProperties(messageBody, message);
            BeanUtil.copyProperties(messageHeader, message);
            BeanUtil.copyProperties(user, message);
            messageList.add(message);
        }
        return messageList;
    }
}
