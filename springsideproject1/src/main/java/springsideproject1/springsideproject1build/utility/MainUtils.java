package springsideproject1.springsideproject1build.utility;

import lombok.experimental.UtilityClass;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
@Transactional
public class MainUtils {
    /**
     * Decode
     */
    public static List<String> decodeUTF8(List<String> list) {
        return list.stream()
                .map(MainUtils::decodeUTF8)
                .collect(Collectors.toList());
    }

    public static String decodeUTF8(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    /**
     * Encode
     */
    public static List<String> encodeUTF8(List<String> list) {
        return list.stream()
                .map(MainUtils::encodeUTF8)
                .collect(Collectors.toList());
    }

    public static String encodeUTF8(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    /**
     * Validate
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }
}
