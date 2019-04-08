package fanmie.com.fanmie;

public class API {


    public static Protocol getAccessToken(String tag) {
        return Protocol.create("/oauth/access_token");
    }

    public static Protocol getData(){
        return Protocol.create("/statuses/home_timeline.json");
    }

}
