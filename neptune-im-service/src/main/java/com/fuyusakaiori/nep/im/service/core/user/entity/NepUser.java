package com.fuyusakaiori.nep.im.service.core.user.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>用户实体类</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepUser {

    /**
     * <h3>用户全局唯一标识符</h3>
     */
    private Integer userId;

    /**
     * <h3>用户登录使用的账号</h3>
     */
    private String userAccount;

    /**
     * <h3>用户登录使用的密码</h3>
     */
    private String userPassword;

    /**
     * <h3>用户昵称</h3>
     */
    private String userNickName;

    /**
     * <h3>用户性别</h3>
     */
    private Integer userGender;

    /**
     * <h3>用户年龄</h3>
     */
    private Integer userAge;

    /**
     * <h3>用户生日</h3>
     */
    private Long userBirthday;

    /**
     * <h3>用户所在地</h3>
     */
    private String userLocation;

    /**
     * <h3>用户个性签名</h3>
     */
    private String userSelfSignature;

    /**
     * <h3>用户允许的好友添加方式: 0 表示允许任何人添加, 1 表示需要验证后添加, 2 表示禁止添加好友</h3>
     */
    private Integer userFriendshipAllowType;

    /**
     * <h3>用户头像地址</h3>
     */
    private String userAvatarAddress;

    /**
     * <h3>用户类型: 0 表示系统管理员, 1 表示普通用户</h3>
     */
    private Integer userType;

    /**
     * <h3>用户是否已经注销: 0 表示没有被注销, 1 表示已经注销</h3>
     */
    private Boolean isDelete;

    /**
     * <h3>拓展字段: 采用 JSON 格式存储</h3>
     */
    private String userExtra;

    private Long createTime;

    private Long updateTime;

}
