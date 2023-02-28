package com.fuyusakaiori.neptune.im.service.core.user.mapper;

import com.fuyusakaiori.neptune.im.service.core.user.entity.NeptuneUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneCancelUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneRegisterUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneQueryUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneEditUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <h3>完全采用 Mybatis 实现的, 没有使用 Mybatis Plus 实现</h3>
 */
@Mapper
public interface INeptuneUserMapper{

    /**
     * <h3>单个用户注册</h3>
     */
    int registerUser(@Param("appId") int appId, @Param("user") NeptuneRegisterUser user);

    /**
     * <h3>批量用户注册</h3>
     */
    int batchRegisterUser(@Param("appId") int appId, @Param("userList") List<NeptuneRegisterUser> userList);

    /**
     * <h3>单个用户注销</h3>
     */
    int cancelUser(@Param("appId") int appId, @Param("user") NeptuneCancelUser user);

    /**
     * <h3>批量用户注销</h3>
     */
    int batchCancelUser(@Param("appId") int appId, @Param("userList") List<NeptuneCancelUser> users);

    /**
     * <h3>单个用户更新</h3>
     * <h4>理论上不存在同时编辑多个用户资料的情况</h4>
     */
    int editUser(@Param("appId")int appId, @Param("user") NeptuneEditUser user);

    /**
     * <h3>查询单个或者多个用户</h3>
     * <h4>查询条件: 上层应用 ID, 用户 ID, 用户性别, 用户是否被封禁, 用户是否注销, 用户类型, 扩展字段</h4>
     */
    List<NeptuneUser> queryUser(@Param("appId") int appId, @Param("user") NeptuneQueryUser user);

    /**
     * <h3>多个用户 ID 查询</h3>
     */
    List<NeptuneUser> queryUserById(@Param("appId") int appId, @Param("userIdList") List<Integer> userIdList);

}
