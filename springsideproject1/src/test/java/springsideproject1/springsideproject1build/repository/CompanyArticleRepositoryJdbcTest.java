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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.utility.ConstantUtils.NAME;

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

    @DisplayName("기사 모두 가져오기")
    @Test
    public void findAll() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

        // when
        articleRepository.insertArticle(article1);
        articleRepository.insertArticle(article2);

        // then
        assertThat(articleRepository.selectArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("기사 번호로 찾기")
    @Test
    public void findByNumber() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

        // when
        article1 = CompanyArticle.builder().article(article1).number(articleRepository.insertArticle(article1)).build();
        article2 = CompanyArticle.builder().article(article2).number(articleRepository.insertArticle(article2)).build();

        // then
        assertThat(articleRepository.selectArticleByNumber(article1.getNumber()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleRepository.selectArticleByNumber(article2.getNumber()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("기사 이름으로 찾기")
    @Test
    public void findByName() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestNewArticle();

        // when
        articleRepository.insertArticle(article1);
        articleRepository.insertArticle(article2);

        // then
        assertThat(articleRepository.selectArticleByName(article1.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(articleRepository.selectArticleByName(article2.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }

    @DisplayName("특정 날짜의 기사 모두 찾기")
    @Test
    public void searchByOneDate() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();

        // when
        articleRepository.insertArticle(article1);
        articleRepository.insertArticle(article2);

        // then
        assertThat(articleRepository.selectArticlesByDate(article1.getDate()))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
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
        articleRepository.insertArticle(article1);
        articleRepository.insertArticle(article2);
        articleRepository.insertArticle(article3);

        // then
        assertThat(articleRepository
                .selectArticlesByDate(sortedArticles.getFirst().getDate(), sortedArticles.getLast().getDate()))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2, article3));
    }

    @DisplayName("기사 저장하기")
    @Test
    public void save() {
        // given
        CompanyArticle article = createTestArticle();

        // when
        articleRepository.insertArticle(article);

        // then
        assertThat(articleRepository.selectArticleByName(article.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("기사 갱신하기")
    @Test
    public void update() {
        // given
        CompanyArticle article = createTestArticle();

        // when
        articleRepository.insertArticle(article);

        // then
        articleRepository.updateArticle(CompanyArticle.builder().article(createTestNewArticle()).name(article.getName()).build());
        assertThat(articleRepository.selectArticleByName(article.getName()).get())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER, NAME)
                .isEqualTo(createTestNewArticle());
    }

    @DisplayName("기사 이름으로 제거하기")
    @Test
    public void removeByName() {
        // given
        CompanyArticle article1 = createTestArticle();
        CompanyArticle article2 = createTestEqualDateArticle();

        // when
        articleRepository.insertArticle(article1);
        articleRepository.insertArticle(article2);

        // then
        articleRepository.deleteArticleByName(article1.getName());
        articleRepository.deleteArticleByName(article2.getName());
        assertThat(articleRepository.selectArticles()).isEmpty();
    }
}