package springsideproject1.springsideproject1build.utility.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.article.CompanyArticleDtoNoNumber;
import springsideproject1.springsideproject1build.domain.article.Press;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.utility.ConstantUtils.NAME;

public interface CompanyArticleTestUtility extends ObjectTestUtility {

    // DB table name
    String companyArticleTable = "testcompanyarticles";

    /**
     * Create
     */
    default CompanyArticle createTestArticle() {
        return CompanyArticle.builder()
                .name("'OLED 위기감' 삼성디스플레이, 주64시간제 도입…삼성 비상경영 확산")
                .press(Press.SBS)
                .subjectCompany("삼성디스플레이")
                .link("https://biz.sbs.co.kr/article/20000176881")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    default CompanyArticleDto createTestArticleDto() {
        return createTestArticle().toDto();
    }

    default CompanyArticleDtoNoNumber createTestArticleDtoNoNumber() {
        return createTestArticle().toDtoNoNumber();
    }

    default CompanyArticle createTestNewArticle() {
        return CompanyArticle.builder()
                .name("[단독] 삼성전자 네트워크사업부 인력 700명 전환 배치")
                .press(Press.HERALD_ECONOMY)
                .subjectCompany("삼성전자")
                .link("https://biz.heraldcorp.com/view.php?ud=20240617050050")
                .date(LocalDate.of(2024, 6, 17))
                .importance(0)
                .build();
    }

    default CompanyArticle createTestEqualDateArticle() {
        return CompanyArticle.builder()
                .name("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회")
                .press(Press.BUSINESS_POST)
                .subjectCompany("삼성전자")
                .link("https://www.businesspost.co.kr/BP?command=article_view&num=355822")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    // These codes consist of two contents, createTestEqualDateArticle and createTestNewArticle
    default List<String> createTestStringArticle() {
        return Arrays.asList("삼성전자",
                String.join(System.lineSeparator(),
                        Arrays.asList("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회",
                                "(2024-6-18, BUSINESS_POST)",
                                "[단독] 삼성전자 네트워크사업부 인력 700명 전환 배치",
                                "(2024-6-17, HERALD_ECONOMY)")),
                String.join(System.lineSeparator(),
                        Arrays.asList("https://www.businesspost.co.kr/BP?command=article_view&num=355822",
                                "https://biz.heraldcorp.com/view.php?ud=20240617050050")));
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder processPostWithCompanyArticle(String url, CompanyArticle article) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", String.valueOf(article.getNumber()))
                .param(NAME, article.getName())
                .param("press", article.getPress().name())
                .param("subjectCompany", article.getSubjectCompany())
                .param("link", article.getLink())
                .param("year", String.valueOf(article.getDate().getYear()))
                .param("month", String.valueOf(article.getDate().getMonthValue()))
                .param("date", String.valueOf(article.getDate().getDayOfMonth()))
                .param("importance", String.valueOf(article.getImportance()));
    }

    default MockHttpServletRequestBuilder processPostWithCompanyArticleDto(String url, CompanyArticleDto articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", String.valueOf(articleDto.getNumber()))
                .param(NAME, articleDto.getName())
                .param("press", articleDto.getPress())
                .param("subjectCompany", articleDto.getSubjectCompany())
                .param("link", articleDto.getLink())
                .param("year", String.valueOf(articleDto.getYear()))
                .param("month", String.valueOf(articleDto.getMonth()))
                .param("date", String.valueOf(articleDto.getDate()))
                .param("importance", String.valueOf(articleDto.getImportance()));
    }

    default MockHttpServletRequestBuilder processPostWithCompanyArticleDtoNoNumber(
            String url, CompanyArticleDtoNoNumber articleDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(NAME, articleDto.getName())
                .param("press", articleDto.getPress())
                .param("subjectCompany", articleDto.getSubjectCompany())
                .param("link", articleDto.getLink())
                .param("year", String.valueOf(articleDto.getYear()))
                .param("month", String.valueOf(articleDto.getMonth()))
                .param("date", String.valueOf(articleDto.getDate()))
                .param("importance", String.valueOf(articleDto.getImportance()));
    }
}
