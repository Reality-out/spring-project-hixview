package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.entity.article.dto.ArticleMainDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.name.ViewName.VIEW_AFTER_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_ARTICLE_MAIN_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_ARTICLE_MAIN_VIEW;
import static site.hixview.support.util.CompanyArticleTestUtils.testCompanyArticle;
import static site.hixview.support.util.CompanyArticleTestUtils.testNewCompanyArticle;
import static site.hixview.support.util.IndustryArticleTestUtils.testIndustryArticle;

public interface ArticleMainTestUtils extends ObjectTestUtils {
    // Assertion
    String addArticleMainProcessPage = ADD_ARTICLE_MAIN_VIEW + VIEW_PROCESS;
    String modifyArticleMainProcessPage = UPDATE_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS;
    String modifyArticleMainFinishUrl = UPDATE_ARTICLE_MAIN_URL + FINISH_URL;

    // Schema Name
    String TEST_ARTICLE_MAINS_SCHEMA = "test_article_mains";

    // Test Object
    ArticleMain testCompanyArticleMain = ArticleMain.builder()
            .name(testCompanyArticle.getName())
            .imagePath("/images/main/newest/company/Samsung_Display_001.png")
            .summary("다급해진 삼성디스플레이, 주당 64시간 근무로 비상경영 돌입")
            .classification(Classification.COMPANY)
            .build();

    ArticleMain testNewCompanyArticleMain = ArticleMain.builder()
            .name(testNewCompanyArticle.getName())
            .imagePath("/images/main/newest/company/Samsung_Electronics_001.png")
            .summary("긴축 경영의 일환인 삼성전자의 네트워크사업부 인력 감축")
            .classification(Classification.COMPANY)
            .build();

    ArticleMain testIndustryArticleMain = ArticleMain.builder()
            .name(testIndustryArticle.getName())
            .imagePath("/images/main/newest/industry/Semiconductor_001.png")
            .summary("미국과 중국의 반도체 관련 디커플링 심화")
            .classification(Classification.INDUSTRY)
            .build();

    /**
     * Create
     */
    default ArticleMainDto createTestCompanyArticleMainDto() {
        return testCompanyArticleMain.toDto();
    }

    default ArticleMainDto createTestNewCompanyArticleMainDto() {
        return testNewCompanyArticleMain.toDto();
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithArticleMain(String url, ArticleMain article) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NUMBER, String.valueOf(article.getNumber()))
                .param(NAME, article.getName())
                .param(IMAGE_PATH, article.getImagePath())
                .param(SUMMARY, article.getSummary())
                .param(ARTICLE_CLASS_NAME, article.getClassification().name());
    }

    default MockHttpServletRequestBuilder postWithArticleMainDto(
            String url, ArticleMainDto articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, articleDto.getName())
                .param(IMAGE_PATH, articleDto.getImagePath())
                .param(SUMMARY, articleDto.getSummary())
                .param(ARTICLE_CLASS_NAME, articleDto.getClassification());
    }
}
