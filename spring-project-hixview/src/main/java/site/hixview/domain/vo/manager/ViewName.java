package site.hixview.domain.vo.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewName {

    // CompanyArticle
    public static final String ADD_COMPANY_ARTICLE_VIEW = "manager/add/article/company/";
    public static final String UPDATE_COMPANY_ARTICLE_VIEW = "manager/update/article/company/";
    public static final String REMOVE_COMPANY_URL_ARTICLE_VIEW = "manager/remove/article/company/";

    // IndustryArticle
    public static final String ADD_INDUSTRY_ARTICLE_VIEW = "manager/add/article/industry/";
    public static final String UPDATE_INDUSTRY_ARTICLE_VIEW = "manager/update/article/industry/";
    public static final String REMOVE_INDUSTRY_ARTICLE_VIEW = "manager/remove/article/industry/";

    // EconomyArticle
    public static final String ADD_ECONOMY_ARTICLE_VIEW = "manager/add/article/economy/";
    public static final String UPDATE_ECONOMY_ARTICLE_VIEW = "manager/update/article/economy/";
    public static final String REMOVE_ECONOMY_ARTICLE_VIEW = "manager/remove/article/economy/";

    // ArticleMain
    public static final String ADD_ARTICLE_MAIN_VIEW = "manager/add/article/main/";
    public static final String UPDATE_ARTICLE_MAIN_VIEW = "manager/update/article/main/";
    public static final String REMOVE_ARTICLE_MAIN_VIEW = "manager/remove/article/main/";

    // Company
    public static final String ADD_COMPANY_VIEW = "manager/add/company/";
    public static final String UPDATE_COMPANY_VIEW = "manager/update/company/";
    public static final String REMOVE_COMPANY_URL_VIEW = "manager/remove/company/";

    // Others
    public static final String MANAGER_HOME_VIEW = "manager/main-page";
    public static final String SELECT_VIEW = "manager/select/";
}
