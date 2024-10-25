package site.hixview.domain.vo.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static site.hixview.domain.vo.RequestPath.BEFORE_PATH_VARIABLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestPath {
    public static final String LOGIN_PATH = "/login";
    public static final String FIND_ID_PATH = "/login/find/id";
    public static final String MEMBERSHIP_PATH = "/membership";
    public static final String COMPANY_SUB_PATH = "/company";
    public static final String COMPANY_SEARCH_PATH = "/company/search" + BEFORE_PATH_VARIABLE;
    public static final String CHECK_PATH = "check" + BEFORE_PATH_VARIABLE;
}