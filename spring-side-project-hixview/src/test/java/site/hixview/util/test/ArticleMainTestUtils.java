package site.hixview.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.ArticleClassName;
import site.hixview.domain.entity.article.ArticleMain;
import site.hixview.domain.entity.article.ArticleMainDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.name.ViewName.AFTER_PROCESS_VIEW;
import static site.hixview.domain.vo.name.ViewName.PROCESS_VIEW;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_ARTICLE_MAIN_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_ARTICLE_MAIN_VIEW;
import static site.hixview.util.test.CompanyArticleTestUtils.testCompanyArticle;
import static site.hixview.util.test.CompanyArticleTestUtils.testNewCompanyArticle;
import static site.hixview.util.test.IndustryArticleTestUtils.testIndustryArticle;

public interface ArticleMainTestUtils extends ObjectTestUtils {
    // Assertion
    String addArticleMainProcessPage = ADD_ARTICLE_MAIN_VIEW + PROCESS_VIEW;
    String modifyArticleMainProcessPage = UPDATE_ARTICLE_MAIN_VIEW + AFTER_PROCESS_VIEW;
    String modifyArticleMainFinishUrl = UPDATE_ARTICLE_MAIN_URL + FINISH_URL;

    // Test Object
    ArticleMain testCompanyArticleMain = ArticleMain.builder()
            .name(testCompanyArticle.getName())
            .imagePath("/images/main/newest/company/Samsung_Display_001.png")
            .summary("다급해진 삼성디스플레이, 주당 64시간 근무로 비상경영 돌입")
            .articleClassName(ArticleClassName.COMPANY_ARTICLE)
            .build();

    ArticleMain testNewCompanyArticleMain = ArticleMain.builder()
            .name(testNewCompanyArticle.getName())
            .imagePath("/images/main/newest/company/Samsung_Electronics_001.png")
            .summary("긴축 경영의 일환인 삼성전자의 네트워크사업부 인력 감축")
            .articleClassName(ArticleClassName.COMPANY_ARTICLE)
            .build();

    ArticleMain testIndustryArticleMain = ArticleMain.builder()
            .name(testIndustryArticle.getName())
            .imagePath("/images/main/newest/industry/Semiconductor_001.png")
            .summary("미국과 중국의 반도체 관련 디커플링 심화")
            .articleClassName(ArticleClassName.INDUSTRY_ARTICLE)
            .build();

    /**
     * Create
     */
    default ArticleMainDto createTestArticleMainDto() {
        return testCompanyArticleMain.toDto();
    }

    default ArticleMainDto createTestNewArticleMainDto() {
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
                .param(ARTICLE_CLASS_NAME, article.getArticleClassName().name());
    }

    default MockHttpServletRequestBuilder postWithArticleMainDto(
            String url, ArticleMainDto articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, articleDto.getName())
                .param(IMAGE_PATH, articleDto.getImagePath())
                .param(SUMMARY, articleDto.getSummary())
                .param(ARTICLE_CLASS_NAME, articleDto.getArticleClassName());
    }
}
