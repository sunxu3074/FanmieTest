package fanmie.com.fanmie.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by chenlongfei on 2016/11/25.
 */

public final class PatternUtils {
    // #话题#
    public static final String REGEX_TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";
    // [表情]
    public static final String REGEX_EMOTION = "\\[(\\S+?)\\]";
    // url
    public static final String REGEX_URL =
            "http[s]?://[a-zA-Z0-9+&@#/%?=~_\\\\-|!:,\\\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
    // @人
    public static final String REGEX_AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";
    //user link
    public static final String REGEX_USER_LINK =
            "@<a href=\"http://fanfou.com/(.*?)\" class=\"former\">(.*?)</a>";

    public static final Pattern PATTERN_TOPIC = compile(REGEX_TOPIC);
    public static final Pattern PATTERN_URL = compile(REGEX_URL);
    public static final Pattern PATTERN_AT = compile(REGEX_AT);

    private PatternUtils() {
    }

    public static String extractToken(String regex, String responseData) {
        Pattern pattern = compile(regex);
        Matcher matcher = pattern.matcher(responseData);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("Can't extract token,responseData is" + responseData);
        }
    }

    //将@用户提取出来
    private static Map<String, String> findAtUser(String sourceText) {
        Map<String, String> userMap = new HashMap<>();
        Pattern pattern = Pattern.compile(REGEX_USER_LINK);
        Matcher matcher = pattern.matcher(sourceText);
        while (matcher.find()) {
            String userId = matcher.group(1);
            String userName = matcher.group(2);
            userMap.put(userName, userId);
        }
        return userMap;
    }


}
