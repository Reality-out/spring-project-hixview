package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleMainDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.UPDATE_COMPANY_ARTICLE_MAIN_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

public interface CompanyArticleMainTestUtils extends ObjectTestUtils {
    // Assertion
    String addArticleMainProcessPage = ADD_COMPANY_ARTICLE_MAIN_VIEW + VIEW_PROCESS_SUFFIX;
    String modifyArticleMainProcessPage = UPDATE_COMPANY_ARTICLE_MAIN_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    String modifyArticleMainFinishUrl = UPDATE_COMPANY_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX;

    // Test Object
    CompanyArticleMain testCompanyArticleMain = CompanyArticleMain.builder()
            .name("시장도 놀란 현대차 통큰 '주주환원'…\"최소 1만원 배당\" 우선주 '껑충'")
            .imagePath("/images/main/newest/company/Hyundai_Motor_Company_001.png")
            .summary("시장 예상치를 상회하는 현대차의 주주가치 제고 방안 발표")
            .build();

    CompanyArticleMain testNewCompanyArticleMain = CompanyArticleMain.builder()
            .name("돈 몰리던 '시총 1위' 어쩌다…24조 증발에 '피눈물' [진영기의 찐개미 찐투자]")
            .imagePath("/images/main/newest/company/Ecopro_BM_001.png")
            .summary("실적도, 전망도 어두운 에코프로비엠")
            .build();

    /**
     * Create
     */
    default CompanyArticleMainDto createTestCompanyArticleMainDto() {
        return testCompanyArticleMain.toDto();
    }

    default CompanyArticleMainDto createTestNewCompanyArticleMainDto() {
        return testNewCompanyArticleMain.toDto();
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithCompanyArticleMain(String url, CompanyArticleMain article) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NUMBER, String.valueOf(article.getNumber()))
                .param(NAME, article.getName())
                .param(IMAGE_PATH, article.getImagePath())
                .param(SUMMARY, article.getSummary());
    }

    default MockHttpServletRequestBuilder postWithCompanyArticleMainDto(
            String url, CompanyArticleMainDto articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, articleDto.getName())
                .param(IMAGE_PATH, articleDto.getImagePath())
                .param(SUMMARY, articleDto.getSummary());
    }
}
