package springsideproject1.springsideproject1build.vo;

import java.util.regex.Pattern;

public class REGEX {
    public static final String URL_REGEX = "^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$";
    public static final Pattern URL_REGEX_PATTERN = Pattern.compile(URL_REGEX);
    public static final Pattern NUMBER_REGEX = Pattern.compile("[0-9]+");
}