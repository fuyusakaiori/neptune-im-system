package com.fuyusakaiori.nep.im.service.support.http;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class NepHttpClient {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig requestConfig;


    /**
     * <h3>http 发送 get 请求: 携带 url</h3>
     */
    public String doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * <h3>http 发送 get 请求: 携带 url + params</h3>
     */
    public String doGet(String url, Map<String, Object> params) throws Exception {
        return doGet(url, params, null);
    }

    /**
     * <h3>http 发送 get 请求: 携带 url + params + charset</h3>
     */
    public String doGet(String url, Map<String, Object> params, String charset) throws Exception {
        return doGet(url,params,null,charset);
    }

    /**
     * <h3>http 发送 get 请求: 携带 url + headers + params + charset</h3>
     */
    public String doGet(String url, Map<String, Object> params, Map<String, Object> headers, String charset) throws Exception {
        // 1. 检查编码方式是否为空, 如果为空则默认使用 utf-8
        if (StringUtils.isEmpty(charset)) {
            charset = CharsetUtil.UTF_8;
        }
        // 2. 校验参数是否为空: 如果不为空就拼接参数
        URIBuilder uriBuilder = new URIBuilder(url);
        if (CollectionUtil.isNotEmpty(params)) {
            params.forEach((key, value) -> uriBuilder.setParameter(key, value.toString()));
        }
        // 3. 生成 http 请求
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        // 4. 设置 http 配置 （http 客户端中配置的）
        httpGet.setConfig(requestConfig);
        // 5. 校验请求头是否为空, 如果不为空就拼接到请求中
        if (CollectionUtil.isNotEmpty(headers)) {
            headers.forEach((key, value) -> httpGet.addHeader(key, value.toString()));
        }
        try {
            String result = StrUtil.EMPTY;
            // 6. http 客户端发出请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            // 7. 校验状态码
            if (response.getStatusLine().getStatusCode() == NepBaseResponseCode.SUCCESS.getCode()) {
                // 8. 解析响应结果
                result = EntityUtils.toString(response.getEntity(), charset);
            }
            return result;
        } catch (IOException exception) {
            throw new RuntimeException("NepHttpClient doGet: 请求发送过程出现异常", exception);
        }
    }

    /**
     * <h3>http 发送 post 请求: 携带 url</h3>
     */
    public String doPost(String url) throws Exception {
        return doPost(url, null,null,null);
    }


    /**
     * <h3>http 发送 post 请求: 携带 url + params + body + charset</h3>
     */
    public String doPost(String url, Map<String, Object> params, String jsonBody, String charset) throws Exception {
        return doPost(url,params,null,jsonBody,charset);
    }

    /**
     * <h3>http 发送 post 请求: 携带 url + headers + params + body + charset</h3>
     */
    public String doPost(String url, Map<String, Object> params, Map<String, Object> headers, String jsonBody, String charset) throws Exception {
        // 1. 检查编码方式是否为空, 如果为空则默认使用 utf-8
        if (StringUtils.isEmpty(charset)) {
            charset = CharsetUtil.UTF_8;
        }
        // 2. 校验参数是否为空: 如果不为空就拼接参数
        URIBuilder uriBuilder = new URIBuilder(url);
        if (CollectionUtil.isNotEmpty(params)) {
            params.forEach((key, value) -> uriBuilder.setParameter(key, value.toString()));
        }
        // 3. 生成 http 请求
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        // 4. 设置 http 配置 （http 客户端中配置的）
        httpPost.setConfig(requestConfig);
        // 5. 校验请求体是否为空, 如果不为空就封装到请求中
        if (StringUtils.isNotEmpty(jsonBody)) {
            StringEntity body = new StringEntity(jsonBody, charset);
            body.setContentEncoding(charset);
            body.setContentType("application/json");
            httpPost.setEntity(body);
        }
        // 6. 校验请求头是否为空, 如果不为空就拼接到请求中
        if (CollectionUtil.isNotEmpty(headers)) {
            headers.forEach((key, value) -> httpPost.addHeader(key, value.toString()));
        }
        try {
            String result = StrUtil.EMPTY;
            // 7. http 客户端发出请求
            CloseableHttpResponse  response = httpClient.execute(httpPost);
            // 8. 校验状态码
            if (response.getStatusLine().getStatusCode() == 200) {
                // 9. 解析响应结果
                result = EntityUtils.toString(response.getEntity(), charset);
            }
            return result;
        } catch (IOException exception) {
            throw new RuntimeException("NepHttpClient doPost: 请求发送过程出现异常", exception);
        }
    }

    /**
     * <h3>http 发送 get 请求: 携带 url + params + charset - 解析成相应的对象并返回</h3>
     */
    public <T> T doGet(String url, Class<T> clazz, Map<String, Object> params, String charSet) throws Exception {
        // 1. 发送 http 请求
        String result = doGet(url, params, charSet);
        // 2. 解析 http 响应
        return StringUtils.isNotEmpty(result) ? JSONUtil.toBean(result, clazz) : null;
    }

     /**
     * <h3>http 发送 get 请求: 携带 url + headers + params + charset - 解析成相应的对象并返回</h3>
     */
    public <T> T doGet(String url, Class<T> clazz, Map<String, Object> params, Map<String, Object> headers, String charSet) throws Exception {
        // 1. 发送 http 请求
        String result = doGet(url, params, headers, charSet);
        // 2. 解析 http 响应
        return StringUtils.isNotEmpty(result) ? JSONUtil.toBean(result, clazz) : null;

    }

    /**
     * <h3>http 发送 post 请求: 携带 url + params + charset - 解析成相应的对象并返回</h3>
     */
    public <T> T doPost(String url, Class<T> clazz, Map<String, Object> params, String jsonBody, String charSet) throws Exception {
        // 1. 发送 http 请求
        String result = doPost(url, params,jsonBody,charSet);
        // 2. 解析 http 响应
        return StringUtils.isNotEmpty(result) ? JSONUtil.toBean(result, clazz) : null;

    }
    /**
     * <h3>http 发送 post 请求: 携带 url + headers + params + charset - 解析成相应的对象并返回</h3>
     */
    public <T> T doPost(String url, Class<T> clazz, Map<String, Object> params, Map<String, Object> headers, String jsonBody, String charSet) throws Exception {
        // 1. 发送 http 请求
        String result = doPost(url, params, headers,jsonBody,charSet);
        // 2. 解析 http 响应
        return StringUtils.isNotEmpty(result) ? JSONUtil.toBean(result, clazz) : null;

    }



}
