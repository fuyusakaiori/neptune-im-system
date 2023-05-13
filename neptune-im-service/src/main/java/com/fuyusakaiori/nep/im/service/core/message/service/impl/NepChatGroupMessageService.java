package com.fuyusakaiori.nep.im.service.core.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.example.nep.im.common.entity.proto.message.NepChatAckMessage;
import com.example.nep.im.common.entity.proto.message.NepChatGroupMessage;
import com.example.nep.im.common.enums.INepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepMessageResponseCode;
import com.example.nep.im.common.enums.message.NepChatGroupMessageType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class NepChatGroupMessageService {

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;

    @Autowired
    private NepCheckSendMessageService checkSendMessageService;

    @Autowired
    private NepStoreSendMessageService storeMessageService;

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    /**
     * <h3>线程池并发处理消息</h3>
     */
    private final ThreadPoolExecutor threadPool;

    // 核心线程数
    public static final int MESSAGE_THREAD_POOL_CORE_POOL_SIZE = 10;

    // 最大线程数
    private static final int MESSAGE_THREAD_POOL_MAXIMUM_POOL_SIZE = 20;

    // 非核心线程存活时间
    public static final int MESSAGE_THREAD_POOL_KEEP_ALIVE_TIME = 60;

    // 阻塞队列大小
    public static final int MESSAGE_THREAD_POOL_CAPACITY = 1000;

    // 初始化线程池
    {
        this.threadPool = new ThreadPoolExecutor(
                MESSAGE_THREAD_POOL_CORE_POOL_SIZE,
                MESSAGE_THREAD_POOL_MAXIMUM_POOL_SIZE,
                MESSAGE_THREAD_POOL_KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MESSAGE_THREAD_POOL_CAPACITY),
                new ThreadFactory(){
                    private final AtomicInteger index = new AtomicInteger(0);
                    @Override
                    public Thread newThread(@Nonnull Runnable runnable) {
                        // 1. 创建线程
                        Thread thread = new Thread(runnable);
                        // 2. 设置线程为守护线程
                        thread.setDaemon(true);
                        // 3. 设置线程名称
                        thread.setName("message-thread-pool-name-" + index.getAndIncrement());
                        // 4. 返回新建的线程
                        return thread;
                    }
                },
                new ThreadPoolExecutor.DiscardPolicy());
    }

    /**
     * <h3>处理群聊消息</h3>
     */
    public void handleMessage(NepChatGroupMessage message) {
        // 1. 获取变量
        int appId = message.getAppId();
        int senderId = message.getSenderId();
        int groupId = message.getGroupId();
        // 2. 参数校验
        if (appId <= 0 || senderId <= 0 || groupId <= 0){
            log.error("NepChatGroupMessageService handleMessage: 群聊消息参数校验失败 - message: {}", message);
            sendAckMessage(message, NepBaseResponseCode.CHECK_PARAM_FAIL);
            return;
        }
        // 3. 发送消息的前置校验
        boolean isAllowSendGroupMessage = checkSendMessageService.checkChatGroupMessageSend(appId, senderId, groupId);
        if (!isAllowSendGroupMessage){
            log.error("NepChatGroupMessageService handleMessage: 群聊消息没有通过前置校验 - message: {}", message);
            sendAckMessage(message, NepMessageResponseCode.CHECK_RELATION_FAIL);
            return;
        }
        // 4. 线程池异步处理消息的分发
        threadPool.execute(()->{
            // 4.1 持久化消息
            storeMessage(message);
            // 4.2 响应 ACK 消息给客户端
            sendAckMessage(message, NepBaseResponseCode.SUCCESS);
            // 4.3 同步消息给自己的所有客户端
            sendSyncMessage(message);
            // 4.4 发送消息给所有群成员的所有客户端
            sendGroupMessage(message);
        });
    }

    /**
     * <h3>持久化消息</h3>
     */
    private void storeMessage(NepChatGroupMessage message){
        storeMessageService.storeMessage(IdUtil.getSnowflakeNextId(), message);
    }

    /**
     * <h3>发送 ACK 消息给发送方</h3>
     */
    private void sendAckMessage(NepChatGroupMessage message, INepBaseResponseCode response){
        // 1. 封装生成响应消息
        NepChatAckMessage chatAckMessage = (NepChatAckMessage) new NepChatAckMessage()
                                             .setCode(response.getCode()).setMessage(response.getMessage()).setMessageId(message.getMessageId())
                                             .setAppId(message.getAppId()).setClientType(message.getClientType()).setImei(message.getImei())
                                             .setMessageType(NepChatGroupMessageType.GROUP_MESSAGE_ACK.getMessageType());
        // 2. 发送消息
        messageSender.sendMessage(message.getAppId(), message.getSenderId(), message.getClientType(), message.getImei(),
                chatAckMessage.getMessageType(), chatAckMessage, false);
    }

    /**
     * <h3>发送同步消息给自己的所有客户端</h3>
     */
    private void sendSyncMessage(NepChatGroupMessage message){
        messageSender.sendMessage(message.getAppId(), message.getSenderId(), message.getClientType(), message.getImei(),
                message.getMessageType(), message, true);
    }

    /**
     * <h3>发送消息给群中的所有群成员</h3>
     */
    private void sendGroupMessage(NepChatGroupMessage message){
        // 1. 查询群组中的所有成员
        List<NepGroupMember> groupMemberList = groupMemberMapper.queryAllGroupMember(message.getAppId(), message.getGroupId());
        // 2. 校验群成员是否为空
        if (CollectionUtil.isEmpty(groupMemberList)){
            log.error("NepChatGroupMessageService sendGroupMessage: 消息将要发送的群聊中没有任何成员 - message: {}", message);
            return;
        }
        // 3. 遍历成员集合
        for (NepGroupMember groupMember : groupMemberList) {
            if (!groupMember.getGroupMemberId().equals(message.getSenderId())){
                messageSender.sendMessage(message.getAppId(), groupMember.getGroupMemberId(),
                        message.getMessageType(), message);
            }
        }
    }


}
