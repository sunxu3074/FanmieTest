package com.driver.http.builder;


import com.driver.http.OkHttpUtils;
import com.driver.http.request.OtherRequest;
import com.driver.http.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
