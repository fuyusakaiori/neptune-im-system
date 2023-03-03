package com.fuyusakaiori.nep.im.service.core.user.mapper;

import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <h3>完全采用 Mybatis 实现的, 没有使用 Mybatis Plus 实现</h3>
 */
@Mapper
public interface INepUserMapper
{

    /**
     * <h3>单个用户注册</h3>
     */
    int registerUser(@Param("appId") int appId, @Param("user") NepRegisterUser user, @Param("createTime") long createTime, @Param("updateTime") long updateTime);

    /**
     * <h3>批量用户注册</h3>
     */
    int batchRegisterUser(@Param("appId") int appId, @Param("userList") List<NepRegisterUser> userList, @Param("createTime") long createTime, @Param("updateTime") long updateTime);

    /**
     * <h3>单个用户更新</h3>
     * <h4>理论上不存在同时编辑多个用户资料的情况</h4>
     */
    int editUser(@Param("appId")int appId, @Param("user") NepEditUser user, @Param("updateTime") long updateTime);

    /**
     * <h3>单个用户注销</h3>
     */
    int cancelUser(@Param("appId") int appId, @Param("userId") int userId, @Param("updateTime") long updateTime);


    /**
     * <h3>查询用户的简易的信息：主要用于校验用户是否存在等状态</h3>
     */
    NepUser querySimpleUserById(@Param("appId") int appId, @Param("userId") Integer userId);

    NepUser querySimpleUserByNickName(@Param("appId") int appId, @Param("nickname") String nickname);

    /**
     * <h3>查询单个用户的简易信息</h3>
     * <h4>场景: 浏览每个用户的详细资料页</h4>
     */
    NepUser queryDetailedUser(@Param("appId") int appId, @Param("userId") int userId, @Param("userNickName") String userNickName);

}