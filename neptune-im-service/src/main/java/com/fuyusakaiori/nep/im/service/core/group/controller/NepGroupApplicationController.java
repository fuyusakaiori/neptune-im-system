package com.fuyusakaiori.nep.im.service.core.group.controller;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepApproveGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepQueryGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepSendGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepApproveGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepQueryGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepSendGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/nep/group/application")
public class NepGroupApplicationController {

    @Autowired
    private INepGroupApplicationService groupApplicationService;


    @PostMapping(value = "/send")
    public NepSendGroupApplicationResponse sendGroupApplication(@RequestBody NepSendGroupApplicationRequest request){
        log.info("NepGroupApplicationController sendGroupApplication: 开始发送入群申请- request: {}", request);
        NepSendGroupApplicationResponse response = groupApplicationService.sendGroupApplication(request);
        log.info("NepGroupApplicationController sendGroupApplication: 入群申请发送结束 - request: {}, response: {}", request, response);
        return response;
    }

    @PostMapping(value = "/approve")
    public NepApproveGroupApplicationResponse approveGroupApplication(@RequestBody NepApproveGroupApplicationRequest request){
        log.info("NepGroupApplicationController approveGroupApplication: 开始审批入群申请 - request: {}", request);
        NepApproveGroupApplicationResponse response = groupApplicationService.approveGroupApplication(request);
        log.info("NepGroupApplicationController approveGroupApplication: 入群申请审批结束 - request: {}, response: {}", request, response);
        return response;
    }

    @GetMapping(value = "/query-all")
    public NepQueryGroupApplicationResponse queryGroupApplication(@RequestParam("appId") int appId, @RequestParam("clientType") int clientType, @RequestParam("imei") String imei,
                                                                  @RequestParam("userId") int userId){
        NepRequestHeader header = new NepRequestHeader()
                                          .setAppId(appId).setImei(imei).setClientType(clientType);
        NepQueryGroupApplicationRequest request = new NepQueryGroupApplicationRequest()
                                                          .setHeader(header).setUserId(userId);
        log.info("NepGroupApplicationController queryGroupApplication: 开始查询所有入群申请 - request: {}", request);
        NepQueryGroupApplicationResponse response = groupApplicationService.queryGroupApplication(request);
        log.info("NepGroupApplicationController queryGroupApplication: 所有入群申请查询结束 - request: {}, response: {}", request, response);
        return response;
    }



}
