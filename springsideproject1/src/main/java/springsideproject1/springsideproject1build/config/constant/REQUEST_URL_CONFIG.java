package springsideproject1.springsideproject1build.config.constant;

public class REQUEST_URL_CONFIG {
    /**
     * Manager
     */

    // Article - Company
    public static final String ADD_SINGLE_COMPANY_ARTICLE_URL = "/manager/article/company/add/single";
    public static final String ADD_COMPANY_ARTICLE_WITH_STRING_URL = "/manager/article/company/add/string";
    public static final String UPDATE_COMPANY_ARTICLE_URL = "/manager/article/company/update";
    public static final String REMOVE_COMPANY_ARTICLE_URL = "/manager/article/company/remove";

    // Member
    public static final String REMOVE_MEMBER_URL = "/manager/member/remove";

    /**
     * Common
     */

    // Prefix
    public static final String URL_REDIRECT_PREFIX = "redirect:";

    // Suffix
    public static final String URL_FINISH_SUFFIX = "/finish";
}