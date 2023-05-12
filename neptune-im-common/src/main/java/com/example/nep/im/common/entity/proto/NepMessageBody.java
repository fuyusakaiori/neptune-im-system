package com.example.nep.im.common.entity.proto;

import com.example.nep.im.common.entity.proto.message.*;
import com.example.nep.im.common.entity.proto.message.friendship.NepAddFriendMessage;
import com.example.nep.im.common.entity.proto.message.friendship.NepRemoveAllFriendMessage;
import com.example.nep.im.common.entity.proto.message.friendship.NepRemoveFriendMessage;
import com.example.nep.im.common.entity.proto.message.group.*;
import com.example.nep.im.common.enums.message.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@ToString
public abstract class NepMessageBody implements Serializable {

    private static final Map<Integer, Class<? extends NepMessageBody>> messageClass = new HashMap<>();

    private int appId;

    private int clientType;

    private int messageType;

    private String imei;

    public static Class<? extends NepMessageBody> getMessageClass(int messageType){
        return messageClass.get(messageType);
    }

    static {
        messageClass.put(NepSystemMessageType.LOGIN.getMessageType(), NepLoginMessage.class);
        messageClass.put(NepSystemMessageType.PING.getMessageType(), NepPingMessage.class);
        messageClass.put(NepSystemMessageType.LOGOUT.getMessageType(), NepLogoutMessage.class);
        messageClass.put(NepChatMessageType.P2P_MESSAGE.getMessageType(), NepChatP2PMessage.class);
        messageClass.put(NepChatMessageType.P2P_MESSAGE_ACK.getMessageType(), NepChatAckMessage.class);
        messageClass.put(NepChatMessageType.P2P_MESSAGE_RECEIVE_ACK.getMessageType(), NepChatConfirmAckMessage.class);
        messageClass.put(NepChatGroupMessageType.GROUP_MESSAGE.getMessageType(), NepChatGroupMessage.class);
        messageClass.put(NepChatGroupMessageType.GROUP_MESSAGE_ACK.getMessageType(), NepChatAckMessage.class);
        messageClass.put(NepFriendshipMessageType.FRIEND_ADD.getMessageType(), NepAddFriendMessage.class);
        messageClass.put(NepFriendshipMessageType.FRIEND_REMOVE.getMessageType(), NepRemoveFriendMessage.class);
        messageClass.put(NepFriendshipMessageType.FRIEND_ALL_REMOVE.getMessageType(), NepRemoveAllFriendMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_EDIT.getMessageType(), NepEditGroupMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_DISSOLVE.getMessageType(), NepDissolveGroupMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_MUTE.getMessageType(), NepMuteGroupMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_TRANSFER.getMessageType(), NepTransferGroupMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_MEMBER_ADD.getMessageType(), NepAddGroupMemberMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_MUTE_MEMBER.getMessageType(), NepMuteGroupMemberMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_MEMBER_ADMIN.getMessageType(), NepChangeGroupMemberTypeMessage.class);
        messageClass.put(NepGroupMessageType.GROUP_APPLICATION_SEND.getMessageType(), NepSendGroupApplicationMessage.class);
    }
}
