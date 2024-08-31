package springsideproject1.springsideproject1build.domain.valueobject;

public abstract class VIEW_NAME {
    /**
     * User
     */

    // Suffix
    public static final String VIEW_SUB_SUFFIX = "subPage";
    public static final String VIEW_SHOW_SUFFIX = "showPage";

    // Others
    public static final String MEMBERSHIP_VIEW = "user/membership/";
    public static final String USER_LOGIN_VIEW = "user/login/";
    public static final String USER_FIND_ID_VIEW = "user/login/findid/";
    public static final String USER_COMPANY_VIEW = "user/company/";

    /**
     * Manager
     */

    // Suffix
    public static final String VIEW_PROCESS_SUFFIX = "processPage";
    public static final String VIEW_FINISH_SUFFIX = "finishPage";
    public static final String VIEW_BEFORE_PROCESS_SUFFIX = "beforeProcessPage";
    public static final String VIEW_AFTER_PROCESS_SUFFIX = "afterProcessPage";
    public static final String VIEW_SINGLE_PROCESS_SUFFIX = "singleProcessPage";
    public static final String VIEW_SINGLE_FINISH_SUFFIX = "singleFinishPage";

    // Others
    public static final String MANAGER_SELECT_VIEW = "manager/select/";

    public static final String ADD_COMPANY_ARTICLE_VIEW = "manager/add/article/company/";
    public static final String UPDATE_COMPANY_ARTICLE_VIEW = "manager/update/article/company/";
    public static final String REMOVE_COMPANY_ARTICLE_VIEW = "manager/remove/article/company/";

    public static final String ADD_COMPANY_ARTICLE_MAIN_VIEW = "manager/add/article/company/main/";
    public static final String UPDATE_COMPANY_ARTICLE_MAIN_VIEW = "manager/update/article/company/main/";
    public static final String REMOVE_COMPANY_ARTICLE_MAIN_VIEW = "manager/remove/article/company/main/";

    public static final String ADD_COMPANY_VIEW = "manager/add/company/";
    public static final String UPDATE_COMPANY_VIEW = "manager/update/company/";
    public static final String REMOVE_COMPANY_VIEW = "manager/remove/company/";
}
