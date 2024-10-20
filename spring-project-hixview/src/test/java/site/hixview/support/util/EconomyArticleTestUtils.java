package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.EconomyArticleBufferSimple;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ECONOMY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_ECONOMY_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_ECONOMY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ViewName.*;

public interface EconomyArticleTestUtils extends ObjectTestUtils {
    // Assertion
    String addSingleEconomyArticleProcessPage = ADD_ECONOMY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
    String addStringEconomyArticleProcessPage = ADD_ECONOMY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
    String modifyEconomyArticleProcessPage = UPDATE_ECONOMY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
    String modifyEconomyArticleFinishUrl = UPDATE_ECONOMY_ARTICLE_URL + FINISH_URL;

    // Schema Name
    String TEST_ECONOMY_ARTICLES_SCHEMA = "test_economy_articles";

    // Test Object
    EconomyArticle testEconomyArticle = EconomyArticle.builder()
            .name("韓, WGBI 26번째 편입…9번째 규모 글로벌 투자처")
            .press(Press.NEWSIS)
            .link("https://www.newsis.com/view/NISX20241009_0002913996")
            .date(LocalDate.of(2024, 10, 9))
            .importance(0)
            .subjectCountry(Country.SOUTH_KOREA)
            .targetEconomyContents("채권", "WGBI")
            .build();

    EconomyArticle testNewEconomyArticle = EconomyArticle.builder()
            .name("“돈 푼다” 공언에 돈 들어왔다, 中 증시 나흘새 8조원 순유입")
            .press(Press.E_DAILY)
            .link("https://m.edaily.co.kr/News/Read?newsId=03499766639025368&mediaCodeNo=257")
            .date(LocalDate.of(2024, 9, 30))
            .importance(0)
            .subjectCountry(Country.CHINA)
            .targetEconomyContents("증시", "부양책")
            .build();

    EconomyArticle testEqualDateEconomyArticle = EconomyArticle.builder()
            .name("산업정책 실패·소극적 투자 … 獨, 유럽경제 문제아로")
            .press(Press.MAEIL_BUSINESS)
            .link("https://www.mk.co.kr/news/world/11135707")
            .date(LocalDate.of(2024, 10, 9))
            .importance(0)
            .subjectCountry(Country.GERMANY)
            .targetEconomyContents("성장", "위기")
            .build();

    EconomyArticleBufferSimple testEqualDateEconomyArticleBuffer = EconomyArticleBufferSimple.builder()
            .articles(testEqualDateEconomyArticle).build();

    EconomyArticleBufferSimple testEconomyArticleBuffer = EconomyArticleBufferSimple.builder()
            .articles(testEqualDateEconomyArticle, testNewEconomyArticle).build();

    /**
     * Create
     */
    default EconomyArticleDto createTestEconomyArticleDto() {
        return testEconomyArticle.toDto();
    }

    default EconomyArticleDto createTestNewEconomyArticleDto() {
        return testNewEconomyArticle.toDto();
    }

    default EconomyArticleDto copyEconomyArticleDto(EconomyArticleDto source) {
        EconomyArticleDto target = new EconomyArticleDto();
        target.setName(source.getName());
        target.setPress(source.getPress());
        target.setLink(source.getLink());
        target.setYear(source.getYear());
        target.setMonth(source.getMonth());
        target.setDays(source.getDays());
        target.setImportance(source.getImportance());
        target.setSubjectCountry(source.getSubjectCountry());
        target.setTargetEconomyContents(source.getTargetEconomyContents());
        return target;
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithEconomyArticle(String url, EconomyArticle article) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NUMBER, String.valueOf(article.getNumber()))
                .param(NAME, article.getName())
                .param(PRESS, article.getPress().name())
                .param(LINK, article.getLink())
                .param(YEAR, String.valueOf(article.getDate().getYear()))
                .param(MONTH, String.valueOf(article.getDate().getMonthValue()))
                .param(DAYS, String.valueOf(article.getDate().getDayOfMonth()))
                .param(IMPORTANCE, String.valueOf(article.getImportance()))
                .param(SUBJECT_COUNTRY, article.getSubjectCountry().name())
                .param(TARGET_ECONOMY_CONTENTS, article.getSerializedTargetEconomyContents());
    }

    default MockHttpServletRequestBuilder postWithEconomyArticleDto(
            String url, EconomyArticleDto articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, articleDto.getName())
                .param(PRESS, articleDto.getPress())
                .param(LINK, articleDto.getLink())
                .param(YEAR, String.valueOf(articleDto.getYear()))
                .param(MONTH, String.valueOf(articleDto.getMonth()))
                .param(DAYS, String.valueOf(articleDto.getDays()))
                .param(IMPORTANCE, String.valueOf(articleDto.getImportance()))
                .param(SUBJECT_COUNTRY, articleDto.getSubjectCountry())
                .param(TARGET_ECONOMY_CONTENTS, articleDto.getTargetEconomyContents());
    }
}
