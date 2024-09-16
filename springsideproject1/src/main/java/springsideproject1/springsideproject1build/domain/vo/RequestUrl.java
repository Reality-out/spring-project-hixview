package springsideproject1.springsideproject1build.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUrl {
    // Prefix
    public static final String REDIRECT_URL = "redirect:";

    // Suffix
    public static final String FINISH_URL = "/finish";
}