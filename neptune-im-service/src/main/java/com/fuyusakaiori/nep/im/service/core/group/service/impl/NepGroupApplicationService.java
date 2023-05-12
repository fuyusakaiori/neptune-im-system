package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepGroupApplicationCode;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepCombineGroupApplication;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepApproveGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepQueryGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepSendGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepApproveGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepQueryGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepSendGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupApplicationService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckGroupApplicationParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NepGroupApplicationService implements INepGroupApplicationService {

    @Autowired
    private NepGroupApplicationServiceImpl groupApplicationServiceImpl;


    @Override
    public NepSendGroupApplicationResponse sendGroupApplication(NepSendGroupApplicationRequest request) {
        NepSendGroupApplicationResponse response = new NepSendGroupApplicationResponse();
        if (!NepCheckGroupApplicationParamUtil.checkNepSendGroupApplicationRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupApplicationService sendGroupApplication: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupApplicationServiceImpl.doSendGroupApplication(request);
            if (result <= 0){
                response.setCode(NepGroupApplicationCode.SEND_APPLICATION_FAIL.getCode())
                        .setMessage(NepGroupApplicationCode.SEND_APPLICATION_FAIL.getMessage());
                log.error("NepGroupApplicationService sendGroupApplication: 入群申请发送失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupApplicationService sendGroupApplication: 入群申请发送成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupApplicationService sendGroupApplication: 发送入群申请出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }

    @Override
    public NepApproveGroupApplicationResponse approveGroupApplication(NepApproveGroupApplicationRequest request) {
        NepApproveGroupApplicationResponse response = new NepApproveGroupApplicationResponse();
        if (!NepCheckGroupApplicationParamUtil.checkNepApproveGroupApplicationRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupApplicationService approveGroupApplication: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupApplicationServiceImpl.doApproveApplication(request);
            if (result <= 0){
                response.setCode(NepGroupApplicationCode.APPROVE_APPLICATION_FAIL.getCode())
                        .setMessage(NepGroupApplicationCode.APPROVE_APPLICATION_FAIL.getMessage());
                log.error("NepGroupApplicationService approveGroupApplication: 审批入群申请失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupApplicationService approveGroupApplication: 审批入群申请成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupApplicationService approveGroupApplication: 审批入群申请出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }

    @Override
    public NepQueryGroupApplicationResponse queryGroupApplication(NepQueryGroupApplicationRequest request) {
        NepQueryGroupApplicationResponse response = new NepQueryGroupApplicationResponse();
        if (!NepCheckGroupApplicationParamUtil.checkNepQueryGroupApplicationRequestParam(request)){
            response.setGroupApplicationList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupApplicationService queryGroupApplication: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepCombineGroupApplication> groupApplicationList = groupApplicationServiceImpl.doQueryGroupApplicationList(request);
            response.setGroupApplicationList(groupApplicationList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupApplicationService queryGroupApplication: 成功查询到群组的所有入群申请 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setGroupApplicationList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupApplicationService queryGroupApplication: 查询群组的所有入群申请失败 - request: {}, response: {}", request, response);
            return response;
        }
    }
}
