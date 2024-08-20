package springsideproject1.springsideproject1build.config.constant;

import java.util.regex.Pattern;

public class REGEX {
    public static final Pattern EMAIL_REGEX = Pattern.compile("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$");
}