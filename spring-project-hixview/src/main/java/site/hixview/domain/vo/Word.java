package site.hixview.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Word {
    // date
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAYS = "days";

    // error
    public static final String CHECK = "check";
    public static final String ERROR = "error";
    public static final String ERROR_SINGLE = "errorSingle";
    public static final String ERRORS_ARE = "errors = {}";

    // layout
    public static final String LAYOUT_PATH = "layoutPath";

    // map
    public static final String NAME = "name";
    public static final String VALUE = "value";
}
