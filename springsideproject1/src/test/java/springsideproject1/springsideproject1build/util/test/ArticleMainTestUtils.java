package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.ArticleClassName;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.*;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.UPDATE_ARTICLE_MAIN_URL;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.domain.vo.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.vo.WORD.NAME;
import static springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils.testCompanyArticle;
import static springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils.testNewCompanyArticle;
import static springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils.testIndustryArticle;

public interface ArticleMainTestUtils extends ObjectTestUtils {
    // Assertion
    String addArticleMainProcessPage = ADD_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
    String modifyArticleMainProcessPage = UPDATE_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    String modifyArticleMainFinishUrl = UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX;

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
