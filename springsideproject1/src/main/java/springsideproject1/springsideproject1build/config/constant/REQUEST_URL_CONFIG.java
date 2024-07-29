package springsideproject1.springsideproject1build.config.constant;

public class REQUEST_URL_CONFIG {
    /**
     * Manager
     */

    // Article
    public static final String ADD_SINGLE_ARTICLE_URL = "/manager/article/add/single";
    public static final String ADD_MULTIPLE_ARTICLE_WITH_STRING_URL = "/manager/article/add/multiple/string";
    public static final String REMOVE_ARTICLE_URL = "/manager/article/remove";

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