package com.example.nep.im.common.entity.proto.message.friendship;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepAddFriendMessage extends NepMessageBody {

    /**
     * <h3>用户全局唯一标识符</h3>
     */
    private Integer userId;

    /**
     * <h3>用户登录使用的账号</h3>
     */
    private String username;

    /**
     * <h3>用户昵称</h3>
     */
    private String nickname;

    /**
     * <h3>用户性别</h3>
     */
    private Integer gender;

    /**
     * <h3>用户个性签名</h3>
     */
    private String selfSignature;

    /**
     * <h3>现居地址</h3>
     */
    private String location;


    /**
     * <h3>用户头像地址</h3>
     */
    private String avatarAddress;

    /**
     * <h3>好友备注</h3>
     */
    private String friendRemark;

    /**
     * <h3>好友所在分组的 ID</h3>
     */
    private Integer groupId;

    /**
     * <h3>好友所在分组的名字</h3>
     */
    private String groupName;

    /**
     * <h3>好友是否被拉黑</h3>
     */
    private boolean black;

}
