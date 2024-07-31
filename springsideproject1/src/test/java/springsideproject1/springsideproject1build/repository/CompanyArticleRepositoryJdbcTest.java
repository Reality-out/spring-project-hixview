package springsideproject1.springsideproject1build.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.utility.test.CompanyArticleTest;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CompanyArticleRepositoryJdbcTest implements CompanyArticleTest {

    @Autowired
    CompanyArticleRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleRepositoryJdbcTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, companyArticleTable, true);
    }

    @DisplayName("기사 모두 가져오기")
    @Test
    public void findAll() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

        // when
        articleRepository.saveOneArticle(article1);
        articleRepository.saveOneArticle(article2);

        // then
        assertThat(articleRepository.findAllArticles())
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("기사 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

        // when
        articleRepository.saveOneArticle(article1);
        articleRepository.saveOneArticle(article2);

        // then
        assertThat(articleRepository.searchArticleByName(article1.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(article1);

        assertThat(articleRepository.searchArticleByName(article2.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(article2);
    }

    @DisplayName("특정 날짜의 기사 모두 찾기")
    @Test
    public void searchByOneDate() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();

        // when
        articleRepository.saveOneArticle(article1);
        articleRepository.saveOneArticle(article2);

        // then
        assertThat(articleRepository.searchArticlesByDate(article1.getDate()))
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("특정 기간의 기사 모두 찾기")
    @Test
    public void searchByDateRange() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();
        CompanyArticle article3 = createTestNewArticle();

        List<CompanyArticle> articles = Arrays.asList(article1, article2, article3);
        List<CompanyArticle> sortedArticles = articles.stream()
                .sorted(Comparator.comparingLong(article -> article.getDate().toEpochDay()))
                .toList();

        // when
        articleRepository.saveOneArticle(article1);
        articleRepository.saveOneArticle(article2);
        articleRepository.saveOneArticle(article3);

        // then
        assertThat(articleRepository
                .searchArticlesByDate(sortedArticles.getFirst().getDate(), sortedArticles.getLast().getDate()))
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(List.of(article1, article2, article3));
    }

    @DisplayName("기사 저장하기")
    @Test
    public void save() {
        // given
        CompanyArticle article = createTestArticle();

        // when
        articleRepository.saveOneArticle(article);

        // then
        assertThat(articleRepository.searchArticleByName(article.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields("number")
                .isEqualTo(article);
    }

    @DisplayName("기사 갱신하기")
    @Test
    public void update() {
        // given
        CompanyArticle article1 = createTestArticle();
        articleRepository.saveOneArticle(article1);

        // when
        article1 = new CompanyArticle.ArticleBuilder()
                .article(createTestNewArticle())
                .name(article1.getName())
                .build();

        // then
        articleRepository.updateOneArticle(article1);
        assertThat(articleRepository.searchArticleByName(article1.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields("number", "name")
                .isEqualTo(createTestNewArticle());
    }

    @DisplayName("기사 이름으로 제거하기")
    @Test
    public void removeByName() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();

        // when
        articleRepository.saveOneArticle(article1);
        articleRepository.saveOneArticle(article2);

        // then
        articleRepository.removeArticleByName(article1.getName());
        articleRepository.removeArticleByName(article2.getName());
        assertThat(articleRepository.findAllArticles()).isEmpty();
    }
}