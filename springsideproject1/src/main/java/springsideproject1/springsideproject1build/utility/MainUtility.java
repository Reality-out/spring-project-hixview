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
public class MainUtility {

    /**
     * Convert
     */
    public static String toStringForUrl(List<?> list) {
        return Pattern.compile("").splitAsStream(list.toString())
                .map(str -> {
                    if (str.equals("[") || str.equals("]") || str.equals(" ")) {
                        return "";
                    } else if (str.equals("!")) {
                        return "%21";
                    } else {
                        return str;
                    }
                }).collect(Collectors.joining());
    }

    /**
     * Decode
     */
    public static List<String> decodeUTF8(List<String> list) {
        list.replaceAll(s -> URLDecoder.decode(s, StandardCharsets.UTF_8));
        return list;
    }

    /**
     * Encode
     */
    public static List<String> encodeUTF8(List<String> list) {
        list.replaceAll(s -> URLEncoder.encode(s, StandardCharsets.UTF_8));
        return list;
    }

    /**
     * Validate
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }
}
