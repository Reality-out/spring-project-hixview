package springsideproject1.springsideproject1build.domain.vo.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUrl {
    public static final String MEMBERSHIP_URL = "/membership";
    public static final String FIND_ID_URL = "/login/find/id";
    public static final String COMPANY_SUB_URL = "/company";
    public static final String COMPANY_SEARCH_URL = "/company/search/";
}