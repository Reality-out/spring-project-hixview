package springsideproject1.springsideproject1build.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.repository.IndustryArticleRepository;
import springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.NUMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_INDUSTRY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@SpringBootTest
@Transactional
class IndustryArticleRepositoryImplTest implements IndustryArticleTestUtils {

    @Autowired
    IndustryArticleRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public IndustryArticleRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLE_TABLE, true);
    }

    @DisplayName("산업 기사들 획득")
    @Test
    public void getIndustryArticlesTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("산업 기사들 날짜로 획득")
    @Test
    public void getIndustryArticleByDateTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testEqualDateIndustryArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticlesByDate(article1.getDate()))
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("산업 기사들 날짜 범위로 획득")
    @Test
    public void getIndustryArticleByDateRangeTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testEqualDateIndustryArticle;
        IndustryArticle article3 = testNewIndustryArticle;

        List<IndustryArticle> articles = List.of(article1, article2, article3);
        List<IndustryArticle> sortedArticles = articles.stream()
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

    @DisplayName("최신 산업 기사들 획득")
    @Test
    public void getLatestIndustryArticlesTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testEqualDateIndustryArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);
        articleRepository.saveArticle(testNewIndustryArticle);

        // then
        assertThat(articleRepository.getLatestArticles())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("번호로 산업 기사 획득")
    @Test
    public void getIndustryArticleByNumberTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;

        // when
        article1 = IndustryArticle.builder().article(article1).number(articleRepository.saveArticle(article1)).build();
        article2 = IndustryArticle.builder().article(article2).number(articleRepository.saveArticle(article2)).build();

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

    @DisplayName("산업 기사 이름으로 획득")
    @Test
    public void getIndustryArticleByNameTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;

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

    @DisplayName("산업 기사 링크로 획득")
    @Test
    public void getIndustryArticleByLinkTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;

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

    @DisplayName("산업 기사 저장")
    @Test
    public void saveIndustryArticleTest() {
        // given
        IndustryArticle article = testIndustryArticle;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article);
    }

    @DisplayName("산업 기사 갱신")
    @Test
    public void updateIndustryArticleTest() {
        // given
        IndustryArticle article = testIndustryArticle;

        // when
        articleRepository.saveArticle(article);

        // then
        articleRepository.updateArticle(IndustryArticle.builder().article(testNewIndustryArticle).name(article.getName()).build());
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER, NAME)
                .isEqualTo(testNewIndustryArticle);
    }

    @DisplayName("산업 기사 이름으로 제거")
    @Test
    public void removeIndustryArticleByNameTest() {
        // given
        IndustryArticle article1 = testIndustryArticle;
        IndustryArticle article2 = testEqualDateIndustryArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        articleRepository.deleteArticleByName(article1.getName());
        articleRepository.deleteArticleByName(article2.getName());
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}