package springsideproject1.springsideproject1build.domain.valueobject;

public abstract class VIEW_NAME {
    /**
     * User
     */

    // Suffix
    public static final String VIEW_SUB_SUFFIX = "sub-page";
    public static final String VIEW_SHOW_SUFFIX = "show-page";

    // Others
    public static final String MEMBERSHIP_VIEW = "user/membership/";
    public static final String USER_LOGIN_VIEW = "user/login/";
    public static final String USER_FIND_ID_VIEW = "user/login/findid/";
    public static final String USER_COMPANY_VIEW = "user/company/";

    public static final String USER_HOME_VIEW = "user/main-page";

    /**
     * Manager
     */

    // Suffix
    public static final String VIEW_PROCESS_SUFFIX = "process-page";
    public static final String VIEW_FINISH_SUFFIX = "finish-page";
    public static final String VIEW_BEFORE_PROCESS_SUFFIX = "before-process-page";
    public static final String VIEW_AFTER_PROCESS_SUFFIX = "after-process-page";
    public static final String VIEW_SINGLE_PROCESS_SUFFIX = "single-process-page";
    public static final String VIEW_SINGLE_FINISH_SUFFIX = "single-finish-page";

    // Article - Company
    public static final String ADD_COMPANY_ARTICLE_VIEW = "manager/add/article/company/";
    public static final String UPDATE_COMPANY_ARTICLE_VIEW = "manager/update/article/company/";
    public static final String REMOVE_COMPANY_ARTICLE_VIEW = "manager/remove/article/company/";

    // Article - Industry
    public static final String ADD_INDUSTRY_ARTICLE_VIEW = "manager/add/article/industry/";
    public static final String UPDATE_INDUSTRY_ARTICLE_VIEW = "manager/update/article/industry/";
    public static final String REMOVE_INDUSTRY_ARTICLE_VIEW = "manager/remove/article/industry/";

    // Article - Main
    public static final String ADD_ARTICLE_MAIN_VIEW = "manager/add/article/main/";
    public static final String UPDATE_ARTICLE_MAIN_VIEW = "manager/update/article/main/";
    public static final String REMOVE_ARTICLE_MAIN_VIEW = "manager/remove/article/main/";

    // Company
    public static final String ADD_COMPANY_VIEW = "manager/add/company/";
    public static final String UPDATE_COMPANY_VIEW = "manager/update/company/";
    public static final String REMOVE_COMPANY_VIEW = "manager/remove/company/";

    // Others
    public static final String MANAGER_HOME_VIEW = "manager/main-page";
    public static final String MANAGER_SELECT_VIEW = "manager/select/";
}
