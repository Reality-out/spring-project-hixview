package springsideproject1.springsideproject1build.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MainUtils {
    /**
     * Decode
     */
    public static List<String> decodeWithUTF8(List<String> list) {
        return list.stream()
                .map(MainUtils::decodeWithUTF8)
                .collect(Collectors.toList());
    }

    public static String decodeWithUTF8(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    /**
     * Encode
     */
    public static List<String> encodeWithUTF8(List<String> list) {
        return list.stream()
                .map(MainUtils::encodeWithUTF8)
                .collect(Collectors.toList());
    }

    public static String encodeWithUTF8(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
}
