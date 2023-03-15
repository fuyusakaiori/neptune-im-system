package com.fuyusakaiori.nep.im.service.core.user.entity.request.normal;


import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>新增用户的请求类</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepRegisterUserRequest {

    /**
     * <h3>请求头</h3>
     */
    private NepRequestHeader header;

    /**
     * <h3>用户账号: 默认用户账号和用户昵称初始时一致</h3>
     */
    private String username;

    /**
     * <h3>用户昵称</h3>
     */
    private String nickname;

    /**
     * <h3>用户密码</h3>
     */
    private String password;

    /**
     * <h3>用户性别</h3>
     */
    private Integer gender;

}
