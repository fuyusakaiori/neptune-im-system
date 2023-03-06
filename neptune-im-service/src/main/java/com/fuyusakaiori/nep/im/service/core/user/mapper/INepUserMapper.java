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
     */
    int editUser(@Param("appId")int appId, @Param("user") NepEditUser user, @Param("updateTime") long updateTime);

    /**
     * <h3>单个用户注销</h3>
     */
    int cancelUser(@Param("appId") int appId, @Param("userId") int userId, @Param("updateTime") long updateTime);


    /**
     * <h3>查询用户: 通过账号查询</h3>
     */
    NepUser queryUserByAccount(@Param("appId") int appId, @Param("account") String account);

    /**
     * <h3>查询用户: 通过昵称查询</h3>
     */
    List<NepUser> queryUserByNickName(@Param("appId") int appId, @Param("nickname") String nickname);

    /**
     * <h3>查询用户: 通过 ID 查询</h3>
     */
    NepUser querySimpleUserById(@Param("appId") int appId, @Param("userId") Integer userId);

    /**
     * <h3>查询用户: 通过账号查询</h3>
     */
    NepUser querySimpleUserByAccount(@Param("appId") int appId, @Param("account") String account);

    /**
     * <h3>查询用户: 通过昵称查询</h3>
     */
    List<NepUser> querySimpleUserByNickName(@Param("appId") int appId, @Param("nickName") String nickName);

    /**
     * <h3>查询所有好友的缩略信息</h3>
     */
    List<NepUser> querySimpleUserByIdList(@Param("appId") int appId, @Param("list") List<Integer> userIdList);

}
