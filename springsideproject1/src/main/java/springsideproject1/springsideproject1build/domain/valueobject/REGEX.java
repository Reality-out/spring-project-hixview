package springsideproject1.springsideproject1build.domain.valueobject;

import java.util.regex.Pattern;

public abstract class REGEX {
    public static final String NUMBER_REGEX = "[0-9]+";
    public static final Pattern NUMBER_REGEX_PATTERN = Pattern.compile(NUMBER_REGEX);
    public static final String URL_REGEX = "^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$";
    public static final Pattern URL_REGEX_PATTERN = Pattern.compile(URL_REGEX);
}