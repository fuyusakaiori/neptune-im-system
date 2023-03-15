package com.fuyusakaiori.nep.im.service.core.server.controller;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.constant.NepZookeeperConstant;
import com.example.nep.im.common.enums.status.NepClientType;
import com.fuyusakaiori.nep.im.service.core.server.entity.request.NepGetServerAddressRequest;
import com.fuyusakaiori.nep.im.service.route.INepLoadBalance;
import com.fuyusakaiori.nep.im.service.core.server.entity.response.NepGetServerAddressResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "nep/server")
public class NepServerAddressController {

    @Autowired
    private INepLoadBalance loadBalance;

    @Autowired
    private CuratorFramework zookeeperClient;

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public NepGetServerAddressResponse getServerAddress(@RequestBody NepGetServerAddressRequest request) throws Exception {
        // 1. 参数校验
        if (Objects.isNull(request)){
            log.error("NepServerAddressController getServerAddress: 请求为空, 前端请传入合法的请求");
            return null;
        }
        // 2. 判断客户端类型, 然后取出对应的服务器结点
        List<String> addressList = request.getClientType() == NepClientType.WEB.getType()
                                           ? zookeeperClient.getChildren().forPath(NepZookeeperConstant.IM_CORE_GATEWAY + NepZookeeperConstant.IM_CORE_GATEWAY_WEB)
                                           : zookeeperClient.getChildren().forPath(NepZookeeperConstant.IM_CORE_GATEWAY + NepZookeeperConstant.IM_CORE_GATEWAY_TCP);
        // 3. 执行路由算法
        String address = loadBalance.select(addressList, String.valueOf(request.getUserId()));
        // 4. 分割地址
        String[] results = address.split(StrUtil.COLON);
        // 5. 封装结果返回
        return new NepGetServerAddressResponse().setAddress(results[0]).setPort(Integer.parseInt(results[1]));
    }


}
