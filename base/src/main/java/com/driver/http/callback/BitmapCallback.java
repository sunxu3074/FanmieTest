package com.driver.http.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class BitmapCallback extends Callback<Bitmap>
{
    @Override
    public Bitmap parseNetworkResponse(Response response , int id) throws Exception
    {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

    @Override
    public Bitmap parseJsonResponse(String response, int id) throws Exception {
        return null;
    }

    @Override
    public Bitmap parseCacheResponse(String response, int id) throws Exception {
        return null;
    }

    @Override
    public void onCacheResponse(Bitmap response) {

    }

    @Override
    public void onError(int ret, int code, String name) {

    }

    @Override
    public void onResponse(Bitmap response) {

    }


}
