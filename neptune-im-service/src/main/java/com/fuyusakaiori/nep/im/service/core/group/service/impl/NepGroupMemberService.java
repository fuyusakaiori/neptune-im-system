package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepGroupMemberService implements INepGroupMemberService {

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;


}
