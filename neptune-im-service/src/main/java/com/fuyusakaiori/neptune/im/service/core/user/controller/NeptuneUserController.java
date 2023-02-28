package com.fuyusakaiori.neptune.im.service.core.user.controller;


import com.fuyusakaiori.neptune.im.service.core.user.service.INeptuneUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "")
public class NeptuneUserController {

    @Autowired
    private INeptuneUserService userService;

}
