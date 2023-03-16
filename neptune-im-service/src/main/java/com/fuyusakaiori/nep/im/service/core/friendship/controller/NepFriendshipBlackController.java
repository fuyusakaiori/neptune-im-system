package com.fuyusakaiori.nep.im.service.core.friendship.controller;

import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipBlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/nep/black")
public class NepFriendshipBlackController {

    @Autowired
    private INepFriendshipBlackService friendshipBlackService;


}
