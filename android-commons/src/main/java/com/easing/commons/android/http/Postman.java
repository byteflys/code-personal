package com.easing.commons.android.http;

import android.os.NetworkOnMainThreadException;

import com.easing.commons.android.clazz.BeanUtil;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.data.Result;
import com.easing.commons.android.event.CommonEvents;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.io.Streams;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.value.http.HttpMethod;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//Postman使用方法：
//设置Url -> 设置Method -> 设置Head，Param，Form，File -> 设置Option
//设置ExceptionHandler -> 设置ResponseHandler -> Execute
@SuppressWarnings("all")
public class Postman {

    //请求参数
    String url;
    HttpMethod method = HttpMethod.GET;

    LinkedHashMap<String, Object> paramMap = new LinkedHashMap();
    LinkedHashMap<String, Object> formMap = new LinkedHashMap();
    LinkedHashMap<String, Object> headMap = new LinkedHashMap();
    LinkedHashMap<File, String> fileMap = new LinkedHashMap();
    LinkedHashMap<byte[], String> byteMap = new LinkedHashMap();
    LinkedHashMap<byte[], String> nameMap = new LinkedHashMap();

    String rawObject;
    String rawBody;

    byte[] binaryBody;

    OnException onException;
    OnException onIoException;
    OnException onBizException;
    OnException onCodeException;
    OnResponse onResponse;

    //Client复用
    boolean useGlobalClient;
    static final OkHttpClient defaultClient;

    //JSON序列化时，是否保留Null字段
    Boolean stringifyWithNull = false;

    //请求构造器
    Request.Builder requestBuilder = new Request.Builder();
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

    //以下变量无实际作用，仅仅方便外部调试
    Call call;
    Response response;
    Throwable exception;
    Integer status;
    Boolean success;

    //执行结果
    public final Result result = Result.success();

    //初始化共用的Client
    static {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(3 * 1000, TimeUnit.MILLISECONDS);
        clientBuilder.readTimeout(60 * 60 * 1000, TimeUnit.MILLISECONDS);
        clientBuilder.writeTimeout(60 * 60 * 1000, TimeUnit.MILLISECONDS);
        clientBuilder.retryOnConnectionFailure(true);
        clientBuilder.connectionPool(new ConnectionPool(100, 5 * 1000, TimeUnit.MILLISECONDS));
        clientBuilder.sslSocketFactory(SSL.sslSocketFactory(), SSL.trustManager());
        clientBuilder.hostnameVerifier(SSL.hostnameVerifier());
        clientBuilder.protocols(Collections.unmodifiableList(Arrays.asList(Protocol.HTTP_1_1, Protocol.SPDY_3, Protocol.HTTP_2)));
        defaultClient = clientBuilder.build();
    }

    //初始化自己的Client
    {
        clientBuilder.connectTimeout(3 * 1000, TimeUnit.MILLISECONDS);
        clientBuilder.readTimeout(60 * 60 * 1000, TimeUnit.MILLISECONDS);
        clientBuilder.writeTimeout(60 * 60 * 1000, TimeUnit.MILLISECONDS);
        clientBuilder.retryOnConnectionFailure(true);
        clientBuilder.connectionPool(new ConnectionPool(100, 5 * 1000, TimeUnit.MILLISECONDS));
        clientBuilder.sslSocketFactory(SSL.sslSocketFactory(), SSL.trustManager());
        clientBuilder.hostnameVerifier(SSL.hostnameVerifier());
        clientBuilder.protocols(Collections.unmodifiableList(Arrays.asList(Protocol.HTTP_1_1, Protocol.SPDY_3, Protocol.HTTP_2)));
    }

    public static Postman create() {
        return new Postman();
    }

    private Postman() {
        useGlobalClient(true);
        useLongConnection(false);
    }

    public Postman url(String url) {
        this.url = url;
        return this;
    }

    public Postman method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public Postman head(String key, Object value) {
        this.headMap.put(key, value);
        return this;
    }

    public Postman head(Map<String, Object> headMap) {
        this.headMap.putAll(headMap);
        return this;
    }

    public Postman param(String key, Object value) {
        this.paramMap.put(key, value);
        return this;
    }

    public Postman param(Map<String, Object> paramMap) {
        this.paramMap.putAll(paramMap);
        return this;
    }

    public Postman param(Object entity) {
        BeanUtil.copyAttribute(entity, paramMap);
        return this;
    }

    public Postman form(String key, Object value) {
        this.formMap.put(key, value);
        return this;
    }

    public Postman form(Map<String, Object> formMap) {
        this.formMap.putAll(formMap);
        return this;
    }

    public Postman form(Object entity) {
        BeanUtil.copyAttribute(entity, formMap);
        return this;
    }

    public Postman file(String key, String file) {
        fileMap.put(new File(file), key);
        return this;
    }

    public Postman file(String key, File file) {
        fileMap.put(file, key);
        return this;
    }

    public Postman bytes(String key, byte[] bytes) {
        byteMap.put(bytes, key);
        return this;
    }

    public Postman name(byte[] bytes, String name) {
        nameMap.put(bytes, name);
        return this;
    }

    public Postman rawTextBody(String body) {
        rawBody = body;
        return this;
    }

    public Postman rawJsonBody(Object body) {
        rawBody = JSON.stringify(body, stringifyWithNull);
        return this;
    }

    public Postman binaryBody(byte[] bytes) {
        binaryBody = bytes;
        return this;
    }

    public Postman binaryBody(InputStream is) {
        binaryBody = Streams.streamToByteArray(is);
        return this;
    }

    public Postman onException(OnException onException) {
        this.onException = onException;
        return this;
    }

    public Postman onIoException(OnException onIoException) {
        this.onIoException = onIoException;
        return this;
    }

    public Postman onBizException(OnException onBizException) {
        this.onBizException = onBizException;
        return this;
    }

    public Postman onCodeException(OnException onCodeException) {
        this.onCodeException = onCodeException;
        return this;
    }

    public Postman onResponse(OnResponse onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public Postman execute(boolean blocking) {
        return blocking ? executeInBlocking() : executeInConcurrent();
    }

    //发布阻塞式请求
    public Postman executeInBlocking() {
        if (Threads.isMainThread()) {
            TipBox.tipInCenterLong("禁止在主线程执行网络请求代码http");
            throw new NetworkOnMainThreadException();
        }
        Response response = null;
        try {
            Postman.this.call = buildCall();
            response = call.execute();
        } catch (UnknownHostException e) {
            Console.error("服务器响应超时", "ConnectException", url);
            Postman.this.exception = e;
            if (onIoException != null)
                onIoException.onException(Postman.this, call, e);
            if (onException != null)
                onException.onException(Postman.this, call, e);
            result.success = false;
            result.data = e;
            result.error = CommonEvents.NETWORK_TIMEOUT;
        } catch (SocketTimeoutException e) {
            Console.error("服务器响应超时", "SocketTimeoutException", url);
            Postman.this.exception = e;
            if (onIoException != null)
                onIoException.onException(Postman.this, call, e);
            if (onException != null)
                onException.onException(Postman.this, call, e);
            result.success = false;
            result.data = e;
            result.error = CommonEvents.NETWORK_TIMEOUT;
        } catch (ConnectException e) {
            Console.error("服务器响应超时", "ConnectException", url);
            Postman.this.exception = e;
            if (onIoException != null)
                onIoException.onException(Postman.this, call, e);
            if (onException != null)
                onException.onException(Postman.this, call, e);
            result.success = false;
            result.data = e;
            result.error = CommonEvents.NETWORK_TIMEOUT;
        } catch (Exception e) {
            Console.error(e);
            Postman.this.exception = e;
            if (onCodeException != null)
                onCodeException.onException(Postman.this, call, e);
            if (onException != null)
                onException.onException(Postman.this, call, e);
            result.success = false;
            result.data = e;
            result.error = CommonEvents.PROGRAM_ERROR;
            result.message = "程序发生未知异常";
        }
        //请求成功，解析结果
        if (response != null) {
            try {
                Postman.this.success = success;
                Postman.this.response = response;
                Postman.this.status = response.code();
                if (onResponse != null)
                    onResponse.onResponse(Postman.this, call, response);
                result.success = true;
                result.message = "成功";
                result.error = null;
            } catch (Throwable e) {
                Console.error(e);
                Postman.this.exception = e;
                if (onBizException != null)
                    onBizException.onException(Postman.this, call, e);
                if (onException != null)
                    onException.onException(Postman.this, call, e);
                result.success = false;
                result.data = e;
                result.error = CommonEvents.SERVER_ERROR;
                result.message = "服务器发生未知异常";
            }
        }
        return this;
    }

    //发布并发式请求
    public Postman executeInConcurrent() {
        //构建Http请求
        try {
            Postman.this.call = buildCall();
        } catch (Exception e) {
            Console.error(e);
            Postman.this.exception = e;
            if (onCodeException != null)
                onCodeException.onException(Postman.this, null, e);
            if (onException != null)
                onException.onException(Postman.this, null, e);
            else
                TipBox.tip("错误的网络请求");
            return this;
        }

        //执行异步请求
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Console.error("网络连接失败", "IOException", url);
                Postman.this.exception = e;
                if (onIoException != null)
                    onIoException.onException(Postman.this, call, e);
                if (onException != null)
                    onException.onException(Postman.this, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    Postman.this.response = response;
                    Postman.this.status = response.code();
                    Postman.this.success = response.code() <= 300;
                    if (onResponse != null)
                        onResponse.onResponse(Postman.this, call, response);
                } catch (Throwable e) {
                    Console.error(e);
                    if (onBizException != null)
                        onBizException.onException(Postman.this, call, e);
                    if (onException != null)
                        onException.onException(Postman.this, call, e);
                    else
                        Console.error("服务器发生异常", "Throwable", url);
                }
            }
        };
        call.enqueue(callback);
        return this;
    }

    //构建最终的Http请求
    private Call buildCall() {
        //设置URL
        url = url + HttpUtils.paramToString(paramMap);
        requestBuilder.url(url);
        //设置请求头
        for (String key : headMap.keySet()) {
            Object value = headMap.get(key);
            if (value != null)
                requestBuilder.header(key, value.toString());
        }
        //设置请求体
        switch (method) {
            case GET: {
                requestBuilder.get();
                break;
            }
            case URL_ENCODED_POST: {
                FormBody formBody = HttpUtils.buildUrlEncodedForm(formMap);
                requestBuilder.post(formBody);
                break;
            }
            case MULTI_FORM_POST: {
                MultipartBody multipartBody = HttpUtils.buildMultiPartForm(formMap, fileMap);
                requestBuilder.post(multipartBody);
                break;
            }
            case MULTI_FORM_POST_BYTE: {
                MultipartBody multipartBody = HttpUtils.buildMultiPartFormWithByte(formMap, byteMap, nameMap);
                requestBuilder.post(multipartBody);
                break;
            }
            case RAW_POST_TEXT: {
                RequestBody requestBody = RequestBody.create(HttpMediaTypes.RAW_TEXT, rawBody);
                requestBuilder.post(requestBody);
                break;
            }
            case RAW_POST_JSON: {
                if (rawBody == null)
                    rawBody = "{}";
                RequestBody requestBody = RequestBody.create(HttpMediaTypes.RAW_JSON, rawBody);
                requestBuilder.post(requestBody);
                break;
            }
            case BINARY_POST: {
                RequestBody requestBody = RequestBody.create(HttpMediaTypes.BINARY, binaryBody);
                requestBuilder.post(requestBody);
                break;
            }
            case PUT: {
                FormBody formBody = HttpUtils.buildUrlEncodedForm(formMap);
                requestBuilder.put(formBody);
                break;
            }
            case PATCH: {
                FormBody formBody = HttpUtils.buildUrlEncodedForm(formMap);
                requestBuilder.patch(formBody);
                break;
            }
            case DELETE: {
                FormBody formBody = HttpUtils.buildUrlEncodedForm(formMap);
                requestBuilder.delete(formBody);
                break;
            }
        }
        //执行请求
        Request request = requestBuilder.build();
        OkHttpClient client = useGlobalClient ? defaultClient : clientBuilder.build();
        Call call = client.newCall(request);
        return call;
    }

    //设置连接超时（与服务器连接成功所需时间）
    public Postman connectTimeOut(long ms) {
        useGlobalClient = false;
        clientBuilder.connectTimeout(ms, TimeUnit.MILLISECONDS);
        return this;
    }

    //设置写数据超时（上传文件，上传请求数据所需时间）
    public Postman writeTimeOut(long ms) {
        useGlobalClient = false;
        clientBuilder.writeTimeout(ms, TimeUnit.MILLISECONDS);
        return this;
    }

    //设置读数据超时（下载文件，读取回复数据所需时间）
    public Postman readTimeOut(long ms) {
        useGlobalClient = false;
        clientBuilder.readTimeout(ms, TimeUnit.MILLISECONDS);
        return this;
    }

    //失败后重新连接
    public Postman retry(boolean retry) {
        useGlobalClient = false;
        clientBuilder.retryOnConnectionFailure(retry);
        return this;
    }

    //使用长连接
    public Postman useLongConnection(boolean use) {
        headMap.put("Connection", use ? "keep-alive" : "close");
        return this;
    }

    //下载字节流的指定部分
    public Postman range(long start, long end) {
        headMap.put("Range", "Bytes=" + start + "-" + end);
        return this;
    }

    //使用默认OkHttp客户端
    //凡是会修改ClientBuilder的功能，都不该使用默认客户端
    public Postman useGlobalClient(boolean useGlobalClient) {
        this.useGlobalClient = useGlobalClient;
        return this;
    }

    public Postman stringifyWithNull(boolean keepNull) {
        this.stringifyWithNull = keepNull;
        return this;
    }

    public static boolean ping(String ip, String port) {
        final Result<Boolean> result = Result.success();
        Postman.create()
                .url("http://" + ip + ":" + port)
                .method(HttpMethod.GET)
                .onIoException((postman, call, e) -> {
                    result.data = false;
                })
                .onCodeException((postman, call, e) -> {
                    result.data = false;
                })
                .onResponse((postman, call, resp) -> {
                    result.data = true;
                })
                .executeInConcurrent();
        long time = 0;
        while (time <= 1500) {
            if (result.data != null)
                return result.data;
            time += 100;
            Threads.sleep(100);
        }
        return false;
    }

    public static boolean pingWithHttps(String ip, String port) {
        final Result<Boolean> result = Result.success();
        Postman.create()
                .url("https://" + ip + ":" + port)
                .method(HttpMethod.GET)
                .onIoException((postman, call, e) -> {
                    result.data = false;
                })
                .onCodeException((postman, call, e) -> {
                    result.data = false;
                })
                .onResponse((postman, call, resp) -> {
                    result.data = true;
                })
                .executeInConcurrent();
        long time = 0;
        while (time <= 1500) {
            if (result.data != null)
                return result.data;
            time += 100;
            Threads.sleep(100);
        }
        return false;
    }

    public interface OnException {
        void onException(Postman postman, Call call, Throwable e);
    }

    public interface OnResponse {
        void onResponse(Postman postman, Call call, Response response) throws Exception;
    }
}
