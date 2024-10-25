package site.hixview.domain.error.nomock;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.service.*;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.ArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.*;

@OnlyRealControllerContext
class UserMainErrorHandleTest implements ArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HomeService homeService;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private EconomyArticleService economyArticleService;

    @Autowired
    private ArticleMainService articleMainService;

    @DisplayName("데이터가 없는 유저 메인 페이지 접속")
    @Test
    void accessUserMainPage() {
        assertThatThrownBy(() -> mockMvc.perform(getWithNoParam(""))).isInstanceOf(ServletException.class).hasMessageContainingAll(NotFoundException.class.getSimpleName(), NO_COMPANY_ARTICLE_WITH_THAT_CONDITION);

        when(companyArticleService.registerArticle(testCompanyArticle)).thenReturn(testCompanyArticle);
        when(articleMainService.registerArticle(testCompanyArticleMain)).thenReturn(testCompanyArticleMain);
        when(homeService.findUsableLatestCompanyArticles()).thenReturn(List.of(testCompanyArticle));
        companyArticleService.registerArticle(testCompanyArticle);
        articleMainService.registerArticle(testCompanyArticleMain);
        assertThatThrownBy(() -> mockMvc.perform(getWithNoParam(""))).isInstanceOf(ServletException.class).hasMessageContainingAll(NotFoundException.class.getSimpleName(), NO_INDUSTRY_ARTICLE_WITH_THAT_CONDITION);

        when(industryArticleService.registerArticle(testIndustryArticle)).thenReturn(testIndustryArticle);
        when(articleMainService.registerArticle(testIndustryArticleMain)).thenReturn(testIndustryArticleMain);
        when(homeService.findUsableLatestIndustryArticles()).thenReturn(List.of(testIndustryArticle));
        industryArticleService.registerArticle(testIndustryArticle);
        articleMainService.registerArticle(testIndustryArticleMain);
        assertThatThrownBy(() -> mockMvc.perform(getWithNoParam(""))).isInstanceOf(ServletException.class).hasMessageContainingAll(NotFoundException.class.getSimpleName(), NO_DOMESTIC_ECONOMY_ARTICLE_WITH_THAT_CONDITION);

        when(economyArticleService.registerArticle(testEconomyArticle)).thenReturn(testEconomyArticle);
        when(articleMainService.registerArticle(testDomesticEconomyArticleMain)).thenReturn(testDomesticEconomyArticleMain);
        when(homeService.findUsableLatestDomesticEconomyArticles()).thenReturn(List.of(testEconomyArticle));
        economyArticleService.registerArticle(testEconomyArticle);
        articleMainService.registerArticle(testDomesticEconomyArticleMain);
        assertThatThrownBy(() -> mockMvc.perform(getWithNoParam(""))).isInstanceOf(ServletException.class).hasMessageContainingAll(NotFoundException.class.getSimpleName(), NO_FOREIGN_ECONOMY_ARTICLE_WITH_THAT_CONDITION);
    }
}
