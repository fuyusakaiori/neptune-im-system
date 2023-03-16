package com.fuyusakaiori.nep.im.service.core.user.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriend {

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
     * <h3>用户个性签名</h3>
     */
    private String selfSignature;

    /**
     * <h3>用户头像地址</h3>
     */
    private String avatarAddress;

    /**
     * <h3>好友备注</h3>
     */
    private String friendRemark;

    /**
     * <h3>好友是否被拉黑</h3>
     */
    private boolean black;

}
