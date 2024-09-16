package springsideproject1.springsideproject1build.domain.vo.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewName {
    public static final String USER_HOME_VIEW = "user/main-page";
    public static final String LOGIN_VIEW = "user/login/";
    public static final String FIND_ID_VIEW = "user/login/findid/";
    public static final String MEMBERSHIP_VIEW = "user/membership/";
    public static final String COMPANY_VIEW = "user/company/";
}
