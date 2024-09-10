package springsideproject1.springsideproject1build.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.company.CompanyArticle;
import springsideproject1.springsideproject1build.domain.repository.CompanyArticleRepository;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@SpringBootTest
@Transactional
class CompanyArticleRepositoryImplTest implements CompanyArticleTestUtils {

    @Autowired
    CompanyArticleRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLE_TABLE, true);
    }

    @DisplayName("기업 기사들 획득")
    @Test
    public void getCompanyArticlesTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("기업 기사들 날짜로 획득")
    @Test
    public void getCompanyArticleByDateTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testEqualDateCompanyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticlesByDate(article1.getDate()))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("기업 기사들 날짜 범위로 획득")
    @Test
    public void getCompanyArticleByDateRangeTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testEqualDateCompanyArticle;
        CompanyArticle article3 = testNewCompanyArticle;

        List<CompanyArticle> articles = List.of(article1, article2, article3);
        List<CompanyArticle> sortedArticles = articles.stream()
                .sorted(Comparator.comparingLong(article -> article.getDate().toEpochDay()))
                .toList();

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);
        articleRepository.saveArticle(article3);

        // then
        assertThat(articleRepository
                .getArticlesByDate(sortedArticles.getFirst().getDate(), sortedArticles.getLast().getDate()))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2, article3));
    }

    @DisplayName("최신 기업 기사들 획득")
    @Test
    public void getLatestCompanyArticlesTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testEqualDateCompanyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);
        articleRepository.saveArticle(testNewCompanyArticle);

        // then
        assertThat(articleRepository.getLatestArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("번호로 기업 기사 획득")
    @Test
    public void getCompanyArticleByNumberTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;

        // when
        article1 = CompanyArticle.builder().article(article1).number(articleRepository.saveArticle(article1)).build();
        article2 = CompanyArticle.builder().article(article2).number(articleRepository.saveArticle(article2)).build();

        // then
        assertThat(articleRepository.getArticleByNumber(article1.getNumber()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByNumber(article2.getNumber()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("기업 기사 이름으로 획득")
    @Test
    public void getCompanyArticleByNameTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticleByName(article1.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByName(article2.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("기업 기사 링크로 획득")
    @Test
    public void getCompanyArticleByLinkTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticleByLink(article1.getLink()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByLink(article2.getLink()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("기업 기사 저장")
    @Test
    public void saveCompanyArticleTest() {
        // given
        CompanyArticle article = testCompanyArticle;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("기업 기사 갱신")
    @Test
    public void updateCompanyArticleTest() {
        // given
        CompanyArticle article = testCompanyArticle;

        // when
        articleRepository.saveArticle(article);

        // then
        articleRepository.updateArticle(CompanyArticle.builder().article(testNewCompanyArticle).name(article.getName()).build());
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER, NAME)
                .isEqualTo(testNewCompanyArticle);
    }

    @DisplayName("기업 기사 이름으로 제거")
    @Test
    public void removeCompanyArticleByNameTest() {
        // given
        CompanyArticle article1 = testCompanyArticle;
        CompanyArticle article2 = testEqualDateCompanyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        articleRepository.deleteArticleByName(article1.getName());
        articleRepository.deleteArticleByName(article2.getName());
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}