package com.fuyusakaiori.nep.im.service.core.group.mapper;

import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepGroupApplicationMapper {


    int sendGroupApplication(@Param("appId") int appId, @Param("application") NepGroupApplication application);

    int approveGroupApplication(@Param("appId") int appId, @Param("applyId") int applyId, @Param("userId") int approveUserId, @Param("status") int status, @Param("updateTime") long updateTime);

    int updateGroupApplication(@Param("appId") int appId, @Param("application") NepGroupApplication application);

    NepGroupApplication queryGroupApplicationById(@Param("appId") int appId, @Param("applyId") int applyId);

    NepGroupApplication queryGroupApplicationByUserAndGroupId(@Param("appId") int appId, @Param("userId") int userId, @Param("groupId") int groupId);

    List<NepGroupApplication> queryGroupApplicationListByGroupIdList(@Param("appId") int appId, @Param("groupIdList") List<Integer> groupIdList);

}
