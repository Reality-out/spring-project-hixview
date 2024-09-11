package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.Press;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleDto;
import springsideproject1.springsideproject1build.domain.entity.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.UPDATE_INDUSTRY_ARTICLE_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.*;

public interface IndustryArticleTestUtils extends ObjectTestUtils {
    // Assertion
    String addSingleIndustryArticleProcessPage = ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS_SUFFIX;
    String addStringIndustryArticleProcessPage = ADD_INDUSTRY_ARTICLE_VIEW + "multiple-string-process-page";
    String modifyIndustryArticleProcessPage = UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS_SUFFIX;
    String modifyIndustryArticleFinishUrl = UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX;

    // Test Object
    IndustryArticle testIndustryArticle = IndustryArticle.builder()
            .name("ASML, 중국 내 장비 유지보수 중단… \"中, 반도체 산업 타격 불가피\"")
            .press(Press.CHOSUN_BIZ)
            .link("https://biz.chosun.com/it-science/ict/2024/08/30/EFI7ZQXPTZE45D53WKE4DNQ6AU/")
            .date(LocalDate.of(2024, 8, 30))
            .importance(0)
            .subjectFirstCategory(FirstCategory.SEMICONDUCTOR)
            .subjectSecondCategory(SecondCategory.SEMICONDUCTOR_MANUFACTURING)
            .build();

    IndustryArticle testNewIndustryArticle = IndustryArticle.builder()
            .name("흔들리는 반도체 굴기…화웨이 \"3·5나노칩 확보 불가\"")
            .press(Press.SEOUL_ECONOMY)
            .link("https://www.sedaily.com/NewsView/2DAECLJTW4")
            .date(LocalDate.of(2024, 6, 9))
            .importance(0)
            .subjectFirstCategory(FirstCategory.SEMICONDUCTOR)
            .subjectSecondCategory(SecondCategory.SEMICONDUCTOR_MANUFACTURING)
            .build();

    IndustryArticle testEqualDateIndustryArticle = IndustryArticle.builder()
            .name("中, AI반도체 수요 급증 속 데이터센터 인프라에 8조원↑ 투자")
            .press(Press.YONHAP_NEWS)
            .link("https://www.yna.co.kr/view/AKR20240830126100009")
            .date(LocalDate.of(2024, 8, 30))
            .importance(0)
            .subjectFirstCategory(FirstCategory.SEMICONDUCTOR)
            .subjectSecondCategory(SecondCategory.SEMICONDUCTOR_MANUFACTURING)
            .build();

    IndustryArticleBufferSimple testEqualDateIndustryArticleStringBuffer = IndustryArticleBufferSimple.builder()
            .articles(testEqualDateIndustryArticle).build();

    IndustryArticleBufferSimple testIndustryArticleStringBuffer = IndustryArticleBufferSimple.builder()
            .articles(testEqualDateIndustryArticle, testNewIndustryArticle).build();

    /**
     * Create
     */
    default IndustryArticleDto createTestIndustryArticleDto() {
        return testIndustryArticle.toDto();
    }

    default IndustryArticleDto createTestNewIndustryArticleDto() {
        return testNewIndustryArticle.toDto();
    }

    default IndustryArticleDto copyIndustryArticleDto(IndustryArticleDto source) {
        IndustryArticleDto target = new IndustryArticleDto();

        target.setName(source.getName());
        target.setPress(source.getPress());
        target.setLink(source.getLink());
        target.setYear(source.getYear());
        target.setMonth(source.getMonth());
        target.setDays(source.getDays());
        target.setImportance(source.getImportance());
        target.setSubjectFirstCategory(source.getSubjectFirstCategory());
        target.setSubjectSecondCategory(source.getSubjectSecondCategory());

        return target;
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithIndustryArticle(String url, IndustryArticle article) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NUMBER, String.valueOf(article.getNumber()))
                .param(NAME, article.getName())
                .param(PRESS, article.getPress().name())
                .param(LINK, article.getLink())
                .param(YEAR, String.valueOf(article.getDate().getYear()))
                .param(MONTH, String.valueOf(article.getDate().getMonthValue()))
                .param(DAYS, String.valueOf(article.getDate().getDayOfMonth()))
                .param(IMPORTANCE, String.valueOf(article.getImportance()))
                .param(SUBJECT_FIRST_CATEGORY, article.getSubjectFirstCategory().name())
                .param(SUBJECT_SECOND_CATEGORY, article.getSubjectSecondCategory().name());
    }

    default MockHttpServletRequestBuilder postWithIndustryArticleDto(
            String url, IndustryArticleDto articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, articleDto.getName())
                .param(PRESS, articleDto.getPress())
                .param(LINK, articleDto.getLink())
                .param(YEAR, String.valueOf(articleDto.getYear()))
                .param(MONTH, String.valueOf(articleDto.getMonth()))
                .param(DAYS, String.valueOf(articleDto.getDays()))
                .param(IMPORTANCE, String.valueOf(articleDto.getImportance()))
                .param(SUBJECT_FIRST_CATEGORY, String.valueOf(articleDto.getSubjectFirstCategory()))
                .param(SUBJECT_SECOND_CATEGORY, String.valueOf(articleDto.getSubjectSecondCategory()));
    }
}
