package fanmie.com.fanmie;

import com.driver.http.OkHttpUtils;
import com.driver.http.builder.PostFormBuilder;
import com.driver.http.request.RequestCall;

import java.io.File;
import java.util.Set;

import fanmie.com.fanmie.utils.XAuthUtils;

/**
 * @author ZZD
 * @time 17/10/18 16:58
 */
public class HttpUtil {


    public static RequestCall post(Protocol protocol) {

        return OkHttpUtils.post().url(protocol.getUrl(Constants.WEB_SERVER_HOST)).params(protocol.getParams()).build();
    }


    public static RequestCall get(Protocol protocol) {
        return OkHttpUtils.get().url(protocol.getUrl(Constants.WEB_SERVER_HOST)).params(protocol.getParams()).build();
    }

    public static RequestCall get(Protocol protocol, String key, String value) {
        return OkHttpUtils.get().url(protocol.getUrl(Constants.WEB_SERVER_HOST))
                .params(protocol.getParams())
                .addParams(key, value)
                .build();
    }

    public static RequestCall postFile(Protocol protocol) {
        PostFormBuilder builder = OkHttpUtils.post().url(protocol.getUrl(Constants.WEB_SERVER_HOST)).params(protocol.getParams());
        if (protocol.getFiles() != null && protocol.getFiles().size() > 0) {
            Set<String> set = protocol.getFiles().keySet();
            File file = null;
            for (String key : set) {
                file = protocol.getFiles().get(key);
                builder.addFile(key, file.getName(), file);
            }

        }

        return builder.build();
    }

    public static RequestCall getToken(Protocol protocol){
        return OkHttpUtils.post()
                .url(protocol.getUrl(Constants.WEB_SERVER_HOST))
                .addHeader(Constants.HEAD_AUTHORIZATION,
                        XAuthUtils.getAuthorization())
                .params(protocol.getParams())
                .build();

    }


}

