package springsideproject1.springsideproject1build.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.utility.test.CompanyArticleTestUtility;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.utility.WordUtils.NAME;

@SpringBootTest
@Transactional
class CompanyArticleRepositoryJdbcTest implements CompanyArticleTestUtility {

    @Autowired
    CompanyArticleRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;
    private final String NUMBER = "number";

    @Autowired
    public CompanyArticleRepositoryJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyArticleTable, true);
    }

    @DisplayName("기업 기사들 획득")
    @Test
    public void getCompanyArticles() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

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
    public void getCompanyArticleByDate() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();

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
    public void getCompanyArticleByDateRange() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();
        CompanyArticle article3 = createTestNewArticle();

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

    @DisplayName("기업 기사 번호로 획득")
    @Test
    public void getCompanyArticleByNumber() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

        // when
        article1 = CompanyArticle.builder().article(article1).number(articleRepository.saveArticle(article1)).build();
        article2 = CompanyArticle.builder().article(article2).number(articleRepository.saveArticle(article2)).build();

        // then
        assertThat(articleRepository.getArticleByNumber(article1.getNumber()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByNumber(article2.getNumber()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("기업 기사 이름으로 획득")
    @Test
    public void getCompanyArticleByName() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticleByName(article1.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByName(article2.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("기업 기사 저장")
    @Test
    public void saveCompanyArticle() {
        // given
        CompanyArticle article = createTestArticle();

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByName(article.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("기업 기사 갱신")
    @Test
    public void updateCompanyArticle() {
        // given
        CompanyArticle article = createTestArticle();

        // when
        articleRepository.saveArticle(article);

        // then
        articleRepository.updateArticle(CompanyArticle.builder().article(createTestNewArticle()).name(article.getName()).build());
        assertThat(articleRepository.getArticleByName(article.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER, NAME)
                .isEqualTo(createTestNewArticle());
    }

    @DisplayName("기업 기사 이름으로 제거")
    @Test
    public void removeCompanyArticleByName() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        articleRepository.deleteArticleByName(article1.getName());
        articleRepository.deleteArticleByName(article2.getName());
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}