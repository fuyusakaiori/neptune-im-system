package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepGroupMemberUser;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NepGroupMemberServiceImpl {

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;


    public int doAddGroupMember(NepAddGroupMemberRequest request) {
        return 0;
    }

    public int doUpdateGroupMember(NepUpdateGroupMemberRequest request) {
        return 0;
    }

    public int doChangeGroupMemberType(NepChangeGroupMemberTypeRequest request) {
        return 0;
    }

    public int doMuteGroupMemberChat(NepMuteGroupMemberRequest request) {
        return 0;
    }

    public int doRevokeGroupMemberChat(NepRevokeGroupMemberRequest request) {
        return 0;
    }

    public int doExitGroupMember(NepExitGroupMemberRequest request) {
        return 0;
    }

    public List<NepGroupMemberUser> doQueryAllGroupMember(NepQueryAllGroupMemberRequest request) {
        return null;
    }
}
