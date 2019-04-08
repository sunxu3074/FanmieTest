package com.driver.http.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        return response.body().string();
    }

    @Override
    public String parseJsonResponse(String response, int id) throws Exception {
        return null;
    }

    @Override
    public String parseCacheResponse(String response, int id) throws Exception {
        return null;
    }

    @Override
    public void onCacheResponse(String response) {

    }

    @Override
    public void onError(int ret, int code, String name) {

    }

    @Override
    public void onResponse(String response) {

    }
}
