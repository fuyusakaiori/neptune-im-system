package com.fuyusakaiori.nep.im.service.support.callback;

public interface INepCallBackService {

    void afterCallBack(int appId, String callbackType, String jsonBody);


    <T> T beforeCallBack(int appId, String callbackType, String jsonBody, Class<T> clazz);

}
