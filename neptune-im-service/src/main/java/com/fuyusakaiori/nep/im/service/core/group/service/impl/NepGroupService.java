package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepGroupService implements INepGroupService {

    @Autowired
    private INepGroupMapper groupMapper;
}
