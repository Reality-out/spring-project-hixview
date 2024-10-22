package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.CompanyArticleBufferSimple;
import site.hixview.domain.entity.article.dto.CompanyArticleDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_COMPANY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_COMPANY_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_COMPANY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.ViewName.*;

public interface CompanyArticleTestUtils extends ObjectTestUtils {
    // Assertion
    String addSingleCompanyArticleProcessPage = ADD_COMPANY_ARTICLE_VIEW + VIEW_SINGLE_PROCESS;
    String addStringCompanyArticleProcessPage = ADD_COMPANY_ARTICLE_VIEW + VIEW_MULTIPLE_STRING_PROCESS;
    String modifyCompanyArticleProcessPage = UPDATE_COMPANY_ARTICLE_VIEW + VIEW_AFTER_PROCESS;
    String modifyCompanyArticleFinishUrl = UPDATE_COMPANY_ARTICLE_URL + FINISH_URL;

    // Schema Name
    String TEST_COMPANY_ARTICLES_SCHEMA = "test_company_articles";

    // Test Object
    CompanyArticle testCompanyArticle = CompanyArticle.builder()
            .name("'OLED 위기감' 삼성디스플레이, 주64시간제 도입…삼성 비상경영 확산")
            .press(Press.SBS)
            .subjectCompany("삼성전자")
            .link("https://biz.sbs.co.kr/article/20000176881")
            .date(LocalDate.of(2024, 6, 18))
            .importance(0)
            .build();

    CompanyArticle testNewCompanyArticle = CompanyArticle.builder()
            .name("[단독] 삼성전자 네트워크사업부 인력 700명 전환 배치")
            .press(Press.HERALD_ECONOMY)
            .subjectCompany("삼성전자")
            .link("https://biz.heraldcorp.com/view.php?ud=20240617050050")
            .date(LocalDate.of(2024, 6, 17))
            .importance(0)
            .build();

    CompanyArticle testEqualDateCompanyArticle = CompanyArticle.builder()
            .name("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회")
            .press(Press.BUSINESS_POST)
            .subjectCompany("삼성전자")
            .link("https://www.businesspost.co.kr/BP?command=article_view&num=355822")
            .date(LocalDate.of(2024, 6, 18))
            .importance(0)
            .build();

    CompanyArticleBufferSimple testEqualDateCompanyArticleBuffer = CompanyArticleBufferSimple.builder()
            .articles(testEqualDateCompanyArticle).build();

    CompanyArticleBufferSimple testCompanyArticleBuffer = CompanyArticleBufferSimple.builder()
            .articles(testEqualDateCompanyArticle, testNewCompanyArticle).build();

    /**
     * Create
     */
    default CompanyArticleDto createTestCompanyArticleDto() {
        return testCompanyArticle.toDto();
    }

    default CompanyArticleDto createTestNewCompanyArticleDto() {
        return testNewCompanyArticle.toDto();
    }

    default CompanyArticleDto copyCompanyArticleDto(CompanyArticleDto source) {
        CompanyArticleDto target = new CompanyArticleDto();
        target.setName(source.getName());
        target.setPress(source.getPress());
        target.setSubjectCompany(source.getSubjectCompany());
        target.setLink(source.getLink());
        target.setYear(source.getYear());
        target.setMonth(source.getMonth());
        target.setDays(source.getDays());
        target.setImportance(source.getImportance());
        return target;
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithCompanyArticle(String url, CompanyArticle article) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NUMBER, String.valueOf(article.getNumber()))
                .param(NAME, article.getName())
                .param(PRESS, article.getPress().name())
                .param(SUBJECT_COMPANY, article.getSubjectCompany())
                .param(LINK, article.getLink())
                .param(YEAR, String.valueOf(article.getDate().getYear()))
                .param(MONTH, String.valueOf(article.getDate().getMonthValue()))
                .param(DAYS, String.valueOf(article.getDate().getDayOfMonth()))
                .param(IMPORTANCE, String.valueOf(article.getImportance()));
    }

    default MockHttpServletRequestBuilder postWithCompanyArticleDto(
            String url, CompanyArticleDto articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, articleDto.getName())
                .param(PRESS, articleDto.getPress())
                .param(SUBJECT_COMPANY, articleDto.getSubjectCompany())
                .param(LINK, articleDto.getLink())
                .param(YEAR, String.valueOf(articleDto.getYear()))
                .param(MONTH, String.valueOf(articleDto.getMonth()))
                .param(DAYS, String.valueOf(articleDto.getDays()))
                .param(IMPORTANCE, String.valueOf(articleDto.getImportance()));
    }
}
