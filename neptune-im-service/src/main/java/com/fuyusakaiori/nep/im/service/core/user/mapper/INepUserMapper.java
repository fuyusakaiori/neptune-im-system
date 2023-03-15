package com.fuyusakaiori.nep.im.service.core.user.mapper;

import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface INepUserMapper {

    /**
     * <h3>注册用户</h3>
     */
    int registerUser(@Param("appId") int appId, @Param("user") NepUser user);

    /**
     * <h3>更新头像地址</h3>
     */
    int updateUserAvatarAddress(@Param("appId") int appId, @Param("userId") int userId, @Param("avatarAddress") String avatarAddress, @Param("updateTime") long updateTime);


    /**
     * <h3>更新用户个人资料</h3>
     */
    int updateUserInfo(@Param("appId")int appId, @Param("user") NepUser user);

    /**
     * <h3>单个用户注销</h3>
     */
    int cancelUser(@Param("appId") int appId, @Param("userId") int userId, @Param("updateTime") long updateTime);


    /**
     * <h3>查询用户: 通过账号查询</h3>
     */
    NepUser queryUserByUserName(@Param("appId") int appId, @Param("username") String username);

    /**
     * <h3>查询用户: 通过昵称查询</h3>
     */
    List<NepUser> queryUserByNickName(@Param("appId") int appId, @Param("nickname") String nickname);

    /**
     * <h3>登陆账号</h3>
     */
    NepUser loginUserInImSystem(@Param("appId") int appId, @Param("username") String username, @Param("password") String password);


    /**
     * <h3>查询用户: 通过 ID 查询</h3>
     */
    NepUser queryUserById(@Param("appId") int appId, @Param("userId") Integer userId);


    /**
     * <h3>查询用户: 根据 ID 集合查询</h3>
     */
    List<NepUser> queryUserByIdList(@Param("appId") int appId, @Param("list") List<Integer> userIdList);

}
