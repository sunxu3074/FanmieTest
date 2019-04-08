package fanmie.com.fanmie;

public class Constants {



    public static final String HEAD_AUTHORIZATION = "Authorization";

    public static final String WEB_SERVER_HOST = "http://fanfou.com";

    public static class FanFou {

        public static final String CONSUMER_KEY = "f44caedd9890e6c2dff609314544245a";
        public static final String CONSUMER_SECRET = "376efb84d711a7e59a9792c1ab8ba1ef";

        public static final String OAUTH_VERSION_VALUE = "1.0";
        public static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
        public static final String X_AUTH_MODE_VALUE = "client_auth";


        public static String OAUTH_TOKEN;
        public static String OAUTH_TOKENSECRET;
        public static String USERNAME;
        public static String PASSWORD;
    }


    public static class XAuth {
        public static final String X_AUTH_USERNAME = "x_auth_username";
        public static final String X_AUTH_PASSWORD = "x_auth_password";
        public static final String X_AUTH_MODE = "x_auth_mode";
        public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
        public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
        public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
        public static final String OAUTH_NONCE = "oauth_nonce";
        public static final String OAUTH_VERSION = "oauth_version";
        public static final String OAUTH_TOKEN = "oauth_token";
        public static final String OAUTH_SIGNATURE = "oauth_signature";
    }

}
