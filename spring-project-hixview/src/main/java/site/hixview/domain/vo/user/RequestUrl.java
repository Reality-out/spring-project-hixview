package site.hixview.domain.vo.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static site.hixview.domain.vo.RequestUrl.BEFORE_PATH_VARIABLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUrl {
    public static final String LOGIN_URL = "/login";
    public static final String FIND_ID_URL = "/login/find/id";
    public static final String MEMBERSHIP_URL = "/membership";
    public static final String COMPANY_SUB_URL = "/company";
    public static final String COMPANY_SEARCH_URL = "/company/search" + BEFORE_PATH_VARIABLE;
    public static final String CHECK_URL = "check" + BEFORE_PATH_VARIABLE;
}