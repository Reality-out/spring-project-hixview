package site.hixview.domain.vo.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestPath {

    // CompanyArticle
    public static final String ADD_SINGLE_COMPANY_ARTICLE_PATH = "/manager/article/company/add/single";
    public static final String ADD_COMPANY_ARTICLE_WITH_STRING_PATH = "/manager/article/company/add/string";
    public static final String SELECT_COMPANY_ARTICLE_PATH = "/manager/article/company/select";
    public static final String UPDATE_COMPANY_ARTICLE_PATH = "/manager/article/company/update";
    public static final String REMOVE_COMPANY_ARTICLE_PATH = "/manager/article/company/remove";

    // IndustryArticle
    public static final String ADD_SINGLE_INDUSTRY_ARTICLE_PATH = "/manager/article/industry/add/single";
    public static final String ADD_INDUSTRY_ARTICLE_WITH_STRING_PATH = "/manager/article/industry/add/string";
    public static final String SELECT_INDUSTRY_ARTICLE_PATH = "/manager/article/industry/select";
    public static final String UPDATE_INDUSTRY_ARTICLE_PATH = "/manager/article/industry/update";
    public static final String REMOVE_INDUSTRY_ARTICLE_PATH = "/manager/article/industry/remove";

    // EconomyArticle
    public static final String ADD_SINGLE_ECONOMY_ARTICLE_PATH = "/manager/article/economy/add/single";
    public static final String ADD_ECONOMY_ARTICLE_WITH_STRING_PATH = "/manager/article/economy/add/string";
    public static final String SELECT_ECONOMY_ARTICLE_PATH = "/manager/article/economy/select";
    public static final String UPDATE_ECONOMY_ARTICLE_PATH = "/manager/article/economy/update";
    public static final String REMOVE_ECONOMY_ARTICLE_PATH = "/manager/article/economy/remove";

    // ArticleMain
    public static final String ADD_ARTICLE_MAIN_PATH = "/manager/article/main/add";
    public static final String SELECT_ARTICLE_MAIN_PATH = "/manager/article/main/select";
    public static final String UPDATE_ARTICLE_MAIN_PATH = "/manager/article/main/update";
    public static final String REMOVE_ARTICLE_MAIN_PATH = "/manager/article/main/remove";
    public static final String CHECK_IMAGE_PATH_ARTICLE_MAIN_PATH = "/manager/article/main/check/imagepath";

    // BlogPost
    public static final String ADD_BLOG_POST_PATH = "/manager/post/blog/add";
    public static final String SELECT_BLOG_POST_PATH = "/manager/post/blog/select";
    public static final String UPDATE_BLOG_POST_PATH = "/manager/post/blog/update";
    public static final String REMOVE_BLOG_POST_PATH = "/manager/post/blog/remove";
    public static final String CHECK_TARGET_IMAGE_PATH_BLOG_POST_PATH = "/manager/post/blog/check/imagepath";

    // Company
    public static final String ADD_SINGLE_COMPANY_PATH = "/manager/company/add/single";
    public static final String SELECT_COMPANY_PATH = "/manager/company/select";
    public static final String UPDATE_COMPANY_PATH = "/manager/company/update";
    public static final String REMOVE_COMPANY_PATH = "/manager/company/remove";

    // Member
    public static final String SELECT_MEMBER_PATH = "/manager/member/select";
}