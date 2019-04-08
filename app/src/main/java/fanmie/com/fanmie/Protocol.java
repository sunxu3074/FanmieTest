package fanmie.com.fanmie;

import android.text.TextUtils;


import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;


public class Protocol {

    private String url = "";

    private String mCurrentUrl = "";
    private LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

    private LinkedHashMap<String, File> files = new LinkedHashMap<String, File>();

    public Protocol(String url) {
        this.url = url;
    }

    public static Protocol create(String url) {
        return new Protocol(url);
    }


    public Protocol add(String k, String v) {
        if (!TextUtils.isEmpty(k)) {
            map.put(k, v);
        }
        return this;
    }

    public Protocol add(String k, File file) {
        if (!TextUtils.isEmpty(k)) {
            files.put(k, file);
        }
        return this;
    }


    public Protocol addMap(Map<String, String> params) {
        map.putAll(params);
        return this;
    }

    public Protocol add(String k, int v) {
        if (!TextUtils.isEmpty(k)) {
            map.put(k, String.valueOf(v));
        }
        return this;
    }

    public String getUrl(String baseUrl) {

//        map.put("carrier", android.os.Build.MANUFACTURER);
//        map.put("model", android.os.Build.MODEL);
        mCurrentUrl = baseUrl + url;
        return mCurrentUrl;
    }

    public String getCurrentUrl(){
        return mCurrentUrl;
    }


    public Map<String, String> getParams() {
        return map;
    }


    public Map<String, File> getFiles() {
        return files;
    }
}
