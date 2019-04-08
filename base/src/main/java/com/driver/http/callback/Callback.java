package com.driver.http.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class Callback<T>
{
    /**
     * UI Thread
     *
     * @param request
     */
    public void onBefore(Request request, int id)
    {
    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter(int id)
    {
    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress, long total , int id)
    {

    }

    /**
     * if you parse reponse code in parseNetworkResponse, you should make this method return true.
     *
     * @param response
     * @return
     */
    public boolean validateReponse(Response response, int id)
    {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response, int id) throws Exception;


    public abstract T parseJsonResponse(String response, int id) throws Exception;

    public abstract void onError(Call call, Exception e, int id);

    public abstract void onResponse(T response, int id);


    public abstract T parseCacheResponse(String response, int id) throws Exception;


    public abstract void onCacheResponse(T response);


    public abstract void onError(int ret, int code, String name);

    public abstract void onResponse(T response);



    public static Callback CALLBACK_DEFAULT = new Callback()
    {

        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception
        {
            return null;
        }

        @Override
        public Object parseJsonResponse(String response, int id) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {

        }

        @Override
        public void onResponse(Object response, int id)
        {

        }

        @Override
        public Object parseCacheResponse(String response, int id) throws Exception {
            return null;
        }

        @Override
        public void onCacheResponse(Object response) {

        }

        @Override
        public void onError(int ret, int code, String name) {

        }

        @Override
        public void onResponse(Object response) {

        }
    };

}