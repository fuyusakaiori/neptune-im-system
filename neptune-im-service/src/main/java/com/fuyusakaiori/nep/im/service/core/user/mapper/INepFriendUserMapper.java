package com.fuyusakaiori.nep.im.service.core.user.mapper;

import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendUserMapper {


    /**
     * <h3>查询单个好友的缩略信息</h3>
     */
    NepUser queryFriendUserById(@Param("appId") int appId, @Param("userId") int userId);

    /**
     * <h3>根据昵称查询单个好友的缩略信息</h3>
     */
    List<NepUser> queryFriendUserByNickName(@Param("appId") int appId, @Param("nickName") String nickName);


    /**
     * <h3>查询所有好友的缩略信息</h3>
     */
    List<NepUser> queryFriendUserByIdList(@Param("appId") int appId, @Param("list") List<Integer> userIdList);

}
