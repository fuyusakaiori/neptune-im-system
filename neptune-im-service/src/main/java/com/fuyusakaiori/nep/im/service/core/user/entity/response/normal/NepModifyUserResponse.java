package com.fuyusakaiori.nep.im.service.core.user.entity.response.normal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h2>新增、删除、更新用户的响应实体类</h2>
 * <h3>响应实体类中有什么字段取决于前台需要什么信息</h3>
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NepModifyUserResponse
{

    /**
     * <h3>响应码</h3>
     */
    private int code;

    /**
     * <h3>响应信息</h3>
     */
    private String message;
}
