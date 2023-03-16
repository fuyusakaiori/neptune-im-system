package com.fuyusakaiori.nep.im.service.core.user.controller;

import com.fuyusakaiori.nep.im.service.core.user.service.INepFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friend")
public class NepFriendController {

    @Autowired
    private INepFriendService friendService;

}
