package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.IndustryArticleBufferSimple;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_INDUSTRY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_INDUSTRY_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_INDUSTRY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ViewName.*;

public interface IndustryArticleTestUtils extends ObjectTestUtils {
    // Assertion
    String addSingleIndustryArticleProcessPage = ADD_INDUSTRY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
    String addStringIndustryArticleProcessPage = ADD_INDUSTRY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
    String modifyIndustryArticleProcessPage = UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
    String modifyIndustryArticleFinishUrl = UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL;

    // Schema Name
    String TEST_INDUSTRY_ARTICLES_SCHEMA = "test_industry_articles";

    // Test Object
    IndustryArticle testIndustryArticle = IndustryArticle.builder()
            .name("ASML, 중국 내 장비 유지보수 중단… \"中, 반도체 산업 타격 불가피\"")
            .press(Press.CHOSUN_BIZ)
            .link("https://biz.chosun.com/it-science/ict/2024/08/30/EFI7ZQXPTZE45D53WKE4DNQ6AU/")
            .date(LocalDate.of(2024, 8, 30))
            .importance(0)
            .subjectFirstCategory(FirstCategory.SEMICONDUCTOR)
            .subjectSecondCategories(SecondCategory.SEMICONDUCTOR_MANUFACTURING)
            .build();

    IndustryArticle testNewIndustryArticle = IndustryArticle.builder()
            .name("흔들리는 반도체 굴기…화웨이 \"3·5나노칩 확보 불가\"")
            .press(Press.SEOUL_ECONOMY)
            .link("https://www.sedaily.com/NewsView/2DAECLJTW4")
            .date(LocalDate.of(2024, 6, 9))
            .importance(0)
            .subjectFirstCategory(FirstCategory.SEMICONDUCTOR)
            .subjectSecondCategories(SecondCategory.SEMICONDUCTOR_MANUFACTURING)
            .build();

    IndustryArticle testEqualDateIndustryArticle = IndustryArticle.builder()
            .name("中, AI반도체 수요 급증 속 데이터센터 인프라에 8조원↑ 투자")
            .press(Press.YONHAP_NEWS)
            .link("https://www.yna.co.kr/view/AKR20240830126100009")
            .date(LocalDate.of(2024, 8, 30))
            .importance(0)
            .subjectFirstCategory(FirstCategory.SEMICONDUCTOR)
            .subjectSecondCategories(SecondCategory.SEMICONDUCTOR_MANUFACTURING)
            .build();

    IndustryArticleBufferSimple testEqualDateIndustryArticleBuffer = IndustryArticleBufferSimple.builder()
            .articles(testEqualDateIndustryArticle).build();

    IndustryArticleBufferSimple testIndustryArticleBuffer = IndustryArticleBufferSimple.builder()
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
        target.setSubjectSecondCategories(source.getSubjectSecondCategories());
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
                .param(SUBJECT_SECOND_CATEGORIES, article.getSerializedSubjectSecondCategories());
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
                .param(SUBJECT_FIRST_CATEGORY, articleDto.getSubjectFirstCategory())
                .param(SUBJECT_SECOND_CATEGORIES, articleDto.getSubjectSecondCategories());
    }
}
