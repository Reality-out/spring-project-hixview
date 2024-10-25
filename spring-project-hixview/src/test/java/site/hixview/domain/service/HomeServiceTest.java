package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.home.ArticleMain;
import site.hixview.domain.repository.ArticleMainRepository;
import site.hixview.domain.repository.CompanyArticleRepository;
import site.hixview.domain.repository.EconomyArticleRepository;
import site.hixview.domain.repository.IndustryArticleRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static site.hixview.domain.entity.home.BlogPost.getFieldNamesWithNoNumber;

@OnlyRealServiceContext
class HomeServiceTest implements ArticleTestUtils, BlogPostTestUtils {

    @Autowired
    private HomeService homeService;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private EconomyArticleService economyArticleService;

    @Autowired
    private CompanyArticleRepository companyArticleRepository;

    @Autowired
    private IndustryArticleRepository industryArticleRepository;

    @Autowired
    private EconomyArticleRepository economyArticleRepository;

    @Autowired
    private ArticleMainRepository articleMainRepository;

    private final String[] fieldNames = getFieldNamesWithNoNumber();

    @DisplayName("사용 가능한 최신 기업 기사들 찾기")
    @Test
    public void findUsableLatestCompanyArticlesTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testEqualDateCompanyArticle;
        ArticleMain articleMain1 = ArticleMain.builder().article(testCompanyArticleMain).name(article1.getName()).build();
        ArticleMain articleMain2 = ArticleMain.builder().article(testNewCompanyArticleMain).name(article2.getName()).build();
        when(companyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(companyArticleRepository.getLatestArticles()).thenReturn(List.of(article1, article2));
        when(articleMainRepository.getArticleByName(articleMain1.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain1));
        when(articleMainRepository.getArticleByName(testNewCompanyArticle.getName())).thenReturn(Optional.empty());
        when(articleMainRepository.getArticleByName(articleMain2.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain2));

        // when
        companyArticleService.registerArticle(article1);
        companyArticleService.registerArticle(testNewCompanyArticle);
        companyArticleService.registerArticle(article2);
        articleMainService.registerArticle(articleMain1);
        articleMainService.registerArticle(articleMain2);

        // then
        assertThat(homeService.findUsableLatestCompanyArticles()).usingRecursiveComparison().isEqualTo(List.of(article1, article2));
    }

    @DisplayName("사용 가능한 최신 산업 기사들 찾기")
    @Test
    public void findUsableLatestIndustryArticlesTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testEqualDateIndustryArticle;
        ArticleMain articleMain1 = ArticleMain.builder().article(testIndustryArticleMain).name(article1.getName()).build();
        ArticleMain articleMain2 = ArticleMain.builder().article(testDomesticEconomyArticleMain).name(article2.getName()).build();
        when(industryArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(industryArticleRepository.getLatestArticles()).thenReturn(List.of(article1, article2));
        when(articleMainRepository.getArticleByName(articleMain1.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain1));
        when(articleMainRepository.getArticleByName(testNewIndustryArticle.getName())).thenReturn(Optional.empty());
        when(articleMainRepository.getArticleByName(articleMain2.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain2));

        // when
        industryArticleService.registerArticle(article1);
        industryArticleService.registerArticle(testNewIndustryArticle);
        industryArticleService.registerArticle(article2);
        articleMainService.registerArticle(articleMain1);
        articleMainService.registerArticle(articleMain2);

        // then
        assertThat(homeService.findUsableLatestIndustryArticles()).usingRecursiveComparison().isEqualTo(List.of(article1, article2));
    }

    @DisplayName("사용 가능한 최신 국내 경제 기사들 찾기")
    @Test
    public void findUsableLatestDomesticEconomyArticles() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testEqualDateEconomyArticle;
        ArticleMain articleMain1 = ArticleMain.builder().article(testDomesticEconomyArticleMain).name(article1.getName()).build();
        ArticleMain articleMain2 = ArticleMain.builder().article(testForeignEconomyArticleMain).name(article2.getName()).build();
        when(economyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(economyArticleRepository.getLatestDomesticArticles()).thenReturn(List.of(article1, article2));
        when(articleMainRepository.getArticleByName(articleMain1.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain1));
        when(articleMainRepository.getArticleByName(testNewEconomyArticle.getName())).thenReturn(Optional.empty());
        when(articleMainRepository.getArticleByName(articleMain2.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain2));

        // when
        economyArticleService.registerArticle(article1);
        economyArticleService.registerArticle(testNewEconomyArticle);
        economyArticleService.registerArticle(article2);
        articleMainService.registerArticle(articleMain1);
        articleMainService.registerArticle(articleMain2);

        // then
        assertThat(homeService.findUsableLatestDomesticEconomyArticles()).usingRecursiveComparison().isEqualTo(List.of(article1, article2));
    }

    @DisplayName("사용 가능한 최신 해외 경제 기사들 찾기")
    @Test
    public void findUsableLatestForeignEconomyArticles() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testEqualDateEconomyArticle;
        ArticleMain articleMain1 = ArticleMain.builder().article(testDomesticEconomyArticleMain).name(article1.getName()).build();
        ArticleMain articleMain2 = ArticleMain.builder().article(testForeignEconomyArticleMain).name(article2.getName()).build();
        when(economyArticleRepository.getArticleByName(any())).thenReturn(Optional.empty());
        when(economyArticleRepository.getLatestForeignArticles()).thenReturn(List.of(article1, article2));
        when(articleMainRepository.getArticleByName(articleMain1.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain1));
        when(articleMainRepository.getArticleByName(testNewEconomyArticle.getName())).thenReturn(Optional.empty());
        when(articleMainRepository.getArticleByName(articleMain2.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(articleMain2));

        // when
        economyArticleService.registerArticle(article1);
        economyArticleService.registerArticle(testNewEconomyArticle);
        economyArticleService.registerArticle(article2);
        articleMainService.registerArticle(articleMain1);
        articleMainService.registerArticle(articleMain2);

        // then
        assertThat(homeService.findUsableLatestForeignEconomyArticles()).usingRecursiveComparison().isEqualTo(List.of(article1, article2));
    }
}