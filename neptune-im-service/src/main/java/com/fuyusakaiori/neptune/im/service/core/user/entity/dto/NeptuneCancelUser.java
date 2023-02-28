package com.fuyusakaiori.neptune.im.service.core.user.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NeptuneCancelUser
{

    /**
     * <h3>需要注销的用户 ID</h3>
     */
    private Integer userId;

    /**
     * <h3>需要注销的用户名称</h3>
     */
    private String userNickName;

}
