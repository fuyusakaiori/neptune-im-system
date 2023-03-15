package com.fuyusakaiori.nep.im.service.core.user.entity.request.normal;


import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>注销用户请求类</h2>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepCancelUserRequest {

    /**
     * <h3>请求头</h3>
     */
    private NepRequestHeader header;

    /**
     * <h3>用户 ID</h3>
     */
    private Integer userId;


}
