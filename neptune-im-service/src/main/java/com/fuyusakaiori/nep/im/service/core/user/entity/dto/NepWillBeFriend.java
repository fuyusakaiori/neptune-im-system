package com.fuyusakaiori.nep.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepWillBeFriend {

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

}
