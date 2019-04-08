package com.driver.http.callback;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class CacheGsonCallback<T> extends Callback<T> {


    boolean cache = true;

    Type mType;


    public CacheGsonCallback() {
        Type myclass = getClass().getGenericSuperclass();    //反射获取带泛型的class
        if (myclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) myclass;      //获取所有泛型
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);  //将泛型转为type
    }


    public CacheGsonCallback(boolean cache) {
        this.cache = cache;
        Type myclass = getClass().getGenericSuperclass();    //反射获取带泛型的class
        if (myclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) myclass;      //获取所有泛型
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);  //将泛型转为type
    }

    public boolean isCache() {
        return cache;
    }

    public final Type getType() {
        return mType;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {

        return null;
    }

    @Override
    public T parseJsonResponse(String response, int id) throws Exception {
        return new Gson().fromJson(response, getType());
    }

    @Override
    public void onResponse(T response, int id) {

    }


    @Override
    public T parseCacheResponse(String response, int id) throws Exception {
        return new Gson().fromJson(response, getType());
    }


//    Type getType(){
//        Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        if(type instanceof Class){
//            return type;
//        }else{
//            return new TypeToken<T>(){}.getType();
//        }
//    }
}
