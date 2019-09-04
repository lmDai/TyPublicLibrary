package com.rrju.library.http;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import com.rrju.library.utils.DevelopLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 开源的OkHttp网络框架自定义类
 */

public class DJLOKHttpClient {
    /**
     * 自定义表单头
     */
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    //  private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//mdiatype 这个需要和服务端保持一致

    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("multipart/form-data; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    /**
     * 网络对象类okHttpClient 实例
     */
    private static OkHttpClient mOkHttpClient;
    private static Handler httpHandler;

    private static OkHttpClient getSyncXYOkHttpClient() {
        if (mOkHttpClient == null) {
            //初始化OkHttpClient
            mOkHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60 * 1000, TimeUnit.SECONDS)//设置超时时间
                    .readTimeout(60 * 1000, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(60 * 1000, TimeUnit.SECONDS)//设置写入超时时间
                    .build();
        }
        if (httpHandler == null) { //初始化Handler
            httpHandler = new Handler(Looper.getMainLooper());
        }
        return mOkHttpClient;
    }

    /**
     * okHttp get异步请求
     *
     * @param url             接口地址
     * @param params          MAP请求参数
     * @param responseHandler 请求返回数据回调
     * @return
     */
    public static void asyncGet(String url, final HttpResponseHandler responseHandler, Map<String, String> params, Activity activity) {
        StringBuilder content = new StringBuilder();
        try {
            int pos = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (pos > 0) {
                    content.append("&");
                }
                content.append(String.format("%s=%s", entry.getKey(), entry.getValue()));
                pos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseHandler.onError(1, "请求参数错误");
        }
        String requestUrl = String.format("%s?%s", url, content.toString());
        getUrl(1, url, content.toString(), "");
        Request request = new Request.Builder().url(requestUrl).tag(activity).build();
        OkHttpClient HttpClient = getSyncXYOkHttpClient();
        Call call = HttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("Canceled")) {
                    //如果是主动取消的情况下
                } else if (e.toString().contains("timed")) {
                    failedCallBack("网络请求超时", responseHandler);
                } else {
                    failedCallBack("请检查网络设置", responseHandler);
                    //其他情况下
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    failedCallBack("访问服务器异常", responseHandler);
                    return;
                }
                try {
                    String responseValue = response.body().string();
                   getUrl(2, "", "", responseValue);
                    successCallBack(responseValue, responseHandler);
                } catch (IOException e) {
                    failedCallBack("获取返回值异常", responseHandler);
                }
            }
        });
    }


    /**
     * okHttp get异步请求  json请求
     *
     * @param url             接口地址
     * @param params          MAP请求参数
     * @param responseHandler 请求返回数据回调
     * @return
     */
    public static void asyncGetJson(String url, final HttpResponseHandler responseHandler, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        //3、将json对象转化为json字符串
        JSONObject jsonObject = new JSONObject(params);

        //修改请求头
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");//mdiatype 这个需要和服务端保持一致
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString());
        getUrl(1, url, jsonObject.toString(), "");
        Request request = new Request.Builder().url(url).post(body).build();
        OkHttpClient HttpClient = getSyncXYOkHttpClient();
        Call call = HttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("Canceled")) {
                    //如果是主动取消的情况下
                } else if (e.toString().contains("timed")) {
                    failedCallBack("网络请求超时", responseHandler);
                } else {
                    failedCallBack("请检查网络设置", responseHandler);
                    //其他情况下
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    failedCallBack("访问服务器异常", responseHandler);
                    return;
                }
                try {
                    String responseValue = response.body().string();
                    getUrl(2, "", "", responseValue);
                    successCallBack(responseValue, responseHandler);
                } catch (IOException e) {
                    failedCallBack("获取返回值异常", responseHandler);
                }
            }
        });
    }

    /**
     * 异步Post
     *
     * @param url
     * @param responseHandler
     * @param params          提交参数数Map型
     * @return
     */
    public static void asyncPost(String url, HttpResponseHandler responseHandler, Map<String, String> params, Activity activity) {
        StringBuilder content = new StringBuilder();
        try {
            int pos = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (pos > 0) {
                    content.append("&");
                }
                content.append(String.format("%s=%s", URLEncoder.encode(entry.getKey(), "UTF-8"), URLEncoder.encode(entry.getValue(), "UTF-8")));
                pos++;
            }
            asyncPost(url, content.toString(), responseHandler, activity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            responseHandler.onError(1, "请求参数错误");
        }
    }


    /**
     * okHttp post异步请求   json请求
     *
     * @param url    接口地址
     * @param params 请求参数
     * @return
     */
    public static void getPostJson(String url, final HttpResponseHandler responseHandler, Map<String, String> params) {
        CacheControl.Builder builder = new CacheControl.Builder();
        builder.maxAge(10, TimeUnit.MILLISECONDS);
        CacheControl cache = builder.build();


        if (params == null) {
            params = new HashMap<>();
        }
        //3、将json对象转化为json字符串
        JSONObject jsonObject = new JSONObject(params);

        //修改请求头
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");//mdiatype 这个需要和服务端保持一致
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString());
        getUrl(1, url, jsonObject.toString(), "");

        Request request = new Request.Builder().url(url)
                .cacheControl(cache).post(body).build();
        OkHttpClient HttpClient = getSyncXYOkHttpClient();

        Call call = HttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("Canceled")) {
                    //如果是主动取消的情况下
                } else if (e.toString().contains("timed")) {
                    failedCallBack("网络请求超时", responseHandler);
                } else {
                    failedCallBack("请检查网络设置", responseHandler);
                    //其他情况下
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    failedCallBack("访问服务器异常", responseHandler);
                    return;
                }
                try {
                    String responseValue = response.body().string();
                    getUrl(2, "", "", responseValue);
                    successCallBack(responseValue, responseHandler);
                } catch (IOException e) {
                    failedCallBack("获取返回值异常", responseHandler);
                }
            }
        });
    }


    /**
     * okHttp post异步请求
     *
     * @param url    接口地址
     * @param params 请求参数
     * @return
     */
    private static void asyncPost(String url, String params, final HttpResponseHandler responseHandler, Activity activity) {
        getUrl(1, url, params, "");
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
        CacheControl.Builder builder = new CacheControl.Builder();
        builder.maxAge(10, TimeUnit.MILLISECONDS);
        CacheControl cache = builder.build();
        Request request = new Request.Builder().url(url).tag(activity).cacheControl(cache).post(body).build();
        OkHttpClient HttpClient = getSyncXYOkHttpClient();
        Call call = HttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("Canceled")) {
                    //如果是主动取消的情况下
                } else if (e.toString().contains("timed")) {
                    failedCallBack("网络请求超时", responseHandler);
                } else {
                    failedCallBack(e.toString(), responseHandler);
                    //其他情况下
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    failedCallBack("访问服务器异常", responseHandler);
                    return;
                }
                try {
                    String responseValue = response.body().string();
                    getUrl(2, "", "", responseValue);
                    successCallBack(responseValue, responseHandler);
                } catch (IOException e) {
                    failedCallBack("获取返回值异常", responseHandler);
                }
            }
        });
    }


    /**
     * 异步Post
     *
     * @param url
     * @param responseHandler
     * @param params          提交参数Map集合类型
     * @return
     */
    public static void upLoadFilePost(String url, final HttpResponseHandler responseHandler, Map<String, String> params, final UploadFile file) {//,Activity activity
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        //追加参数
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
        }
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public long contentLength() {
                return file.getFile().length();
            }


            @Override
            public void writeTo(BufferedSink sink) {
                try {
                    Source source = Okio.source(file.getFile());
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 1024)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        int percentsProgress = (int) (current * 100 / remaining);
                        progressCallBack(percentsProgress, responseHandler);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        builder.addFormDataPart(file.getFormName(), file.getFile().getName(), requestBody);
        //创建RequestBody
        RequestBody body = builder.build();
        //创建Request      addHeader 添加请求头
        Request request = new Request.Builder().url(url).post(body).build();//.tag(activity)
        OkHttpClient HttpClient = getSyncXYOkHttpClient();
        Call call = HttpClient.newBuilder().writeTimeout(60 * 1000, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("Canceled")) {
                    //如果是主动取消的情况下
                } else if (e.toString().contains("timed")) {
                    failedCallBack("网络请求超时", responseHandler);
                } else {
                    failedCallBack("请检查网络设置", responseHandler);
                    //其他情况下
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    failedCallBack("访问服务器异常", responseHandler);
                    return;
                }
                try {
                    String responseValue = response.body().string();
                    successCallBack(responseValue, responseHandler);
                } catch (IOException e) {
                    failedCallBack("获取返回值异常", responseHandler);
                }

            }
        });
    }


    /**
     * 带进度下载文件
     *
     * @param downUrl         下载链接url
     * @param file            本地文件对象
     * @param responseHandler 请求返回数据回调
     */
    public static void downLoadFile(String downUrl, final File file, final HttpResponseHandler responseHandler) {
        Request request = new Request.Builder().url(downUrl).build();
        OkHttpClient HttpClient = getSyncXYOkHttpClient();
        Call call = HttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.toString().contains("Canceled")) {
                    //如果是主动取消的情况下
                } else if (e.toString().contains("timed")) {
                    failedCallBack("网络请求超时", responseHandler);
                } else {
                    failedCallBack("请检查网络设置", responseHandler);
                    //其他情况下
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    failedCallBack("访问服务器异常", responseHandler);
                    return;
                }
                InputStream is = null;
                byte[] buf = new byte[1024];
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    int len = 0;
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        int percentsProgress = (int) (current * 100 / total);
                        progressCallBack(percentsProgress, responseHandler);
                    }
                    fos.flush();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("filePath", file.getAbsolutePath());
                        responseHandler.onSuccess(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    successCallBack(file.getAbsolutePath(), responseHandler);
                } catch (IOException e) {
                    failedCallBack("获取返回值异常", responseHandler);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        failedCallBack("获取返回值异常", responseHandler);
                    }
                }
            }
        });
    }


    /**
     * @param url        下载链接
     * @param startIndex 下载起始位置
     * @param endIndex   结束为止
     * @param callback   回调
     * @throws IOException
     */
    public static void downloadFileByRange(String url, long startIndex, long endIndex, Callback callback) {
        // 创建一个Request
        // 设置分段下载的头信息。 Range:做分段数据请求,断点续传指示下载的区间。格式: Range bytes=0-1024或者bytes:0-1024
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex)
                .url(url)
                .build();
        doAsync(request, callback);
    }

    public static void getContentLength(String url, Callback callback) {

        // 创建一个Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        doAsync(request, callback);
    }

    /**
     * 异步请求
     */
    private static void doAsync(Request request, Callback callback) {
        OkHttpClient HttpClient = getSyncXYOkHttpClient();
        //创建请求会话
        Call call = HttpClient.newCall(request);
        //同步执行会话请求
        call.enqueue(callback);
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     */
    private static void failedCallBack(final String errorMsg, final HttpResponseHandler responseHandler) {
        httpHandler.post(new Runnable() {
            @Override
            public void run() {
                responseHandler.onError(1, errorMsg);
            }
        });
    }

    /**
     * 统一同意处理成功信息
     *
     * @param response
     */
    private static void successCallBack(final Object response, final HttpResponseHandler responseHandler) {
        httpHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    responseHandler.onSuccess(result);
                } catch (JSONException e) {
                    responseHandler.onError(2, e.getMessage());
                }
            }
        });
    }

    /**
     * 统一处理进度信息
     *
     * @param progress 当前进度
     */
    private static void progressCallBack(final int progress, final HttpResponseHandler responseHandler) {
        httpHandler.post(new Runnable() {
            @Override
            public void run() {
                responseHandler.onProgress(progress);
            }
        });
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(Object tag) {
        if (getSyncXYOkHttpClient() == null || tag == null) return;
        for (Call call : getSyncXYOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getSyncXYOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
    public static void getUrl(int type, String url, String params, String object) {
        if (!TextUtils.isEmpty(object)) {
            object = object.replace("\\", "");
        }

        if (type == 1) {
            DevelopLog.d("==", "请求路径：" + url);
            DevelopLog.d("==", "请求参数：" + params);
        } else {
            DevelopLog.d("==", "返回参数：" + object);
        }
    }

}
