package com.fuyusakaiori.nep.im.service.util.callback;


import org.springframework.stereotype.Component;

@Component
public class NepCommonCallBackService implements INepCallBackService{

    @Override
    public void afterCallBack(int appId, String callbackType, String jsonBody) {

    }

    @Override
    public <T> T beforeCallBack(int appId, String callbackType, String jsonBody, Class<T> clazz) {
        return null;
    }
}
