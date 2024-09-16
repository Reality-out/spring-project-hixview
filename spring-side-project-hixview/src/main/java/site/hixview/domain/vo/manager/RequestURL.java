package site.hixview.domain.vo.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestURL {

    // CompanyArticle
    public static final String ADD_SINGLE_COMPANY_ARTICLE_URL = "/manager/article/company/add/single";
    public static final String ADD_COMPANY_ARTICLE_WITH_STRING_URL = "/manager/article/company/add/string";
    public static final String SELECT_COMPANY_ARTICLE_URL = "/manager/article/company/select";
    public static final String UPDATE_COMPANY_ARTICLE_URL = "/manager/article/company/update";
    public static final String REMOVE_COMPANY_ARTICLE_URL = "/manager/article/company/remove";

    // IndustryArticle
    public static final String ADD_SINGLE_INDUSTRY_ARTICLE_URL = "/manager/article/industry/add/single";
    public static final String ADD_INDUSTRY_ARTICLE_WITH_STRING_URL = "/manager/article/industry/add/string";
    public static final String SELECT_INDUSTRY_ARTICLE_URL = "/manager/article/industry/select";
    public static final String UPDATE_INDUSTRY_ARTICLE_URL = "/manager/article/industry/update";
    public static final String REMOVE_INDUSTRY_ARTICLE_URL = "/manager/article/industry/remove";

    // ArticleMain
    public static final String ADD_ARTICLE_MAIN_URL = "/manager/article/main/add";
    public static final String SELECT_ARTICLE_MAIN_URL = "/manager/article/main/select";
    public static final String UPDATE_ARTICLE_MAIN_URL = "/manager/article/main/update";
    public static final String REMOVE_ARTICLE_MAIN_URL = "/manager/article/main/remove";

    // Company
    public static final String ADD_SINGLE_COMPANY_URL = "/manager/company/add/single";
    public static final String SELECT_COMPANY_URL = "/manager/company/select";
    public static final String UPDATE_COMPANY_URL = "/manager/company/update";
    public static final String REMOVE_COMPANY_URL = "/manager/company/remove";

    // Member
    public static final String SELECT_MEMBER_URL = "/manager/member/select";
}