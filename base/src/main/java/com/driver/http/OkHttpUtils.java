package com.driver.http;

import android.text.TextUtils;

import com.driver.http.builder.GetBuilder;
import com.driver.http.builder.HeadBuilder;
import com.driver.http.builder.OtherRequestBuilder;
import com.driver.http.builder.PostFileBuilder;
import com.driver.http.builder.PostFormBuilder;
import com.driver.http.builder.PostStringBuilder;
import com.driver.http.cache.DiskLruCacheHelper;
import com.driver.http.callback.CacheGsonCallback;
import com.driver.http.callback.Callback;
import com.driver.http.callback.GsonCallback;
import com.driver.http.request.RequestCall;
import com.driver.http.utils.Platform;
import com.driver.util.UtilsLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;


    public DiskLruCacheHelper getDiskLruCacheHelper() {
        return diskLruCacheHelper;
    }

    public OkHttpUtils setDiskLruCacheHelper(DiskLruCacheHelper diskLruCacheHelper) {
        this.diskLruCacheHelper = diskLruCacheHelper;
        return this;
    }

    public interface OnLogoutListener {

        void onLogout();

        void onError(String msg);

    }

    public OnLogoutListener getOnLogoutListener() {
        return onLogoutListener;
    }

    public OkHttpUtils setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
        return this;
    }

    private OnLogoutListener onLogoutListener;

    private DiskLruCacheHelper diskLruCacheHelper;


    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();


    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }


    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        UtilsLog.e("x", "request      ==    " + requestCall.getRequest().url().toString());
        UtilsLog.e("x", "params      ==    " + requestCall.getOkHttpRequest().getParams());


        if (callback instanceof CacheGsonCallback && diskLruCacheHelper != null) {
            String json = diskLruCacheHelper.getAsString(requestCall.getRequest().url().toString());

            CacheGsonCallback cacheCallback = (CacheGsonCallback) callback;

            if (!TextUtils.isEmpty(json) && cacheCallback.isCache()) {
                Object o = null;
                try {
                    o = finalCallback.parseCacheResponse(json, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendSuccessResultCacheCallback(o, finalCallback, id);
            }


        }


        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }


                    if (!finalCallback.validateReponse(response, id)) {

                        UtilsLog.e("x", "response code      ==    " + response.code());
                        if (response.code() == 403) {

                            mPlatform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    if (onLogoutListener != null) {
                                        onLogoutListener.onLogout();
                                    }
                                }
                            });

                        } else {
//                            Log.e("x", "---------sendFailResultCallback---------------------");
                            sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        }

                        return;
                    }


                    if (finalCallback instanceof GsonCallback) {

                        String responseStr = response.body().string();
                        if(isJson(responseStr)){
                            JSONObject resultJO = new JSONObject(responseStr);
                            UtilsLog.e("x", "response      ==    " + responseStr);
                            int ret = resultJO.optInt("ret");

                            if (ret == 200) {
                                Object data = resultJO.opt("data");

                                Object o;
                                if (data instanceof String) {
                                    o = data;
                                } else {
                                    o = finalCallback.parseJsonResponse(data.toString(), id);
                                }

                                sendSuccessResultCallback(finalCallback, id, o);
                            } else {
                                int code = resultJO.optInt("code");
                                final String name = resultJO.optString("msg");

                                if (ret == 500 && (code == 10020 || code == 10022)) {
                                    mPlatform.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (onLogoutListener != null) {
                                                onLogoutListener.onError(name);
                                            }
                                        }
                                    });

                                } else {
                                    sendFailResultCallback(finalCallback, ret, code, name, id);
                                }
                            }
                        } else {
                            sendSuccessResultCallback(finalCallback, id, responseStr);
                        }



                    } else if (finalCallback instanceof CacheGsonCallback && diskLruCacheHelper != null) {

                        String responseStr = response.body().string();
                        JSONObject resultJO = new JSONObject(responseStr);

                        UtilsLog.e("x", "response      ==    " + responseStr);

                        int ret = resultJO.optInt("ret");
                        if (ret == 200) {

                            Object data = resultJO.opt("data");

                            Object o;
                            if (data instanceof String) {
                                o = data;
                            } else {
                                o = finalCallback.parseJsonResponse(data.toString(), id);
                            }
                            sendSuccessResultCallback(finalCallback, id, o);

                            String key = requestCall.getRequest().url().toString();

                            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(diskLruCacheHelper.getAsString(key))) {
                                diskLruCacheHelper.remove(key);
                            }
                            CacheGsonCallback cacheCallback = (CacheGsonCallback) finalCallback;
                            if (cacheCallback.isCache()) {
                                diskLruCacheHelper.put(key, data.toString());
                            }

                        } else {
                            int code = resultJO.optInt("code");
                            final String name = resultJO.optString("msg");
                            if (ret == 500 && (code == 10020 || code == 10022)) {
                                mPlatform.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (onLogoutListener != null) {
                                            onLogoutListener.onError(name);
                                        }
                                    }
                                });

                            } else {
                                sendFailResultCallback(finalCallback, ret, code, name, id);
                            }


                        }

                    } else {
                        Object o = finalCallback.parseNetworkResponse(response, id);
                        sendSuccessResultCallback(o, finalCallback, id);
                    }


                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, id);
                    e.printStackTrace();

                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }


    public void sendFailResultCallback(final Callback callback, final int ret, final int code, final String name, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(ret, code, name);
                callback.onAfter(id);
            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }


    public void sendSuccessResultCallback(final Callback callback, final int id, final Object object) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter(id);
            }
        });
    }


    public void sendSuccessResultCacheCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onCacheResponse(object);
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }

    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

}

