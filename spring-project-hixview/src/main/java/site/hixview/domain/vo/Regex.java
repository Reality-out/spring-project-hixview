package site.hixview.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Regex {
    // Regex
    public static final String ID_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$";
    public static final String MESSAGE_REGEX = "^(\\{)?(.*[a-zA-Z]).(.*[a-zA-Z]).(.*[a-zA-Z0-9])}?$";
    public static final String NAME_REGEX = "^(?=.{2,30}$)([가-힣a-zA-Z]+\\s?[가-힣a-zA-Z]+|[가-힣a-zA-Z]{2,})$";
    public static final String NUMBER_REGEX = "^[0-9]+$";
    public static final String PHONE_NUMBER_REGEX = "^(010-([0-9]{4})-([0-9]{4}))|(010([0-9]{8}))$";
    public static final String PW_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,64}$";
    public static final String URL_REGEX =
            "^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}[-a-zA-Z0-9()@:%_+.~#?&/=]*$";

    // Pattern
    public static final Pattern MESSAGE_PATTERN = Pattern.compile(MESSAGE_REGEX);
    public static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);
}