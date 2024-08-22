package springsideproject1.springsideproject1build.vo;

import java.util.regex.Pattern;

public class REGEX {
    public static final Pattern EMAIL_REGEX = Pattern.compile("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$");
    public static final Pattern NUMBER_REGEX = Pattern.compile("[0-9]+");
}