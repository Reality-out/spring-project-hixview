package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS_NAME.COMPANY_ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.UPDATE_ARTICLE_MAIN_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

public interface ArticleMainTestUtils extends ObjectTestUtils {
    // Assertion
    String addArticleMainProcessPage = ADD_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
    String modifyArticleMainProcessPage = UPDATE_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    String modifyArticleMainFinishUrl = UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX;

    // Test Object
    ArticleMain testArticleMain = ArticleMain.builder()
            .name("시장도 놀란 현대차 통큰 '주주환원'…\"최소 1만원 배당\" 우선주 '껑충'")
            .imagePath("/images/main/newest/company/Hyundai_Motor_Company_001.png")
            .summary("시장 예상치를 상회하는 현대차의 주주가치 제고 방안 발표")
            .articleClassName(COMPANY_ARTICLE)
            .build();

    ArticleMain testNewArticleMain = ArticleMain.builder()
            .name("돈 몰리던 '시총 1위' 어쩌다…24조 증발에 '피눈물' [진영기의 찐개미 찐투자]")
            .imagePath("/images/main/newest/company/Ecopro_BM_001.png")
            .summary("실적도, 전망도 어두운 에코프로비엠")
            .articleClassName(COMPANY_ARTICLE)
            .build();

    /**
     * Create
     */
    default ArticleMainDto createTestArticleMainDto() {
        return testArticleMain.toDto();
    }

    default ArticleMainDto createTestNewArticleMainDto() {
        return testNewArticleMain.toDto();
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
                .param(ARTICLE_CLASS_NAME, article.getArticleClassName());
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
