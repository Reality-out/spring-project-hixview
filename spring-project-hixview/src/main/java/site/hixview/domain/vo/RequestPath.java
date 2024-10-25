package site.hixview.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestPath {
    // Prefix
    public static final String RELATIVE_REDIRECT_PATH = "redirect:";

    // Suffix
    public static final String BEFORE_PATH_VARIABLE = "/";
    public static final String FINISH_PATH = "/finish";

    // Others
    public static final String ROOT_PATH = System.getProperty("user.dir");
    public static final String STATIC_RESOURCE_PATH = "src/main/resources/static";
}