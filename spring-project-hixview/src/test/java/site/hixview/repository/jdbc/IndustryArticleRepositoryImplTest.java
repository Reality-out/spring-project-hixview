package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.repository.IndustryArticleRepository;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.util.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.name.EntityName.Article.NUMBER;

@OnlyRealRepositoryContext
class IndustryArticleRepositoryImplTest implements IndustryArticleTestUtils {

    @Autowired
    private IndustryArticleRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    IndustryArticleRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLES_SCHEMA, true);
    }

    @DisplayName("산업 기사들 획득")
    @Test
    void getIndustryArticlesTest() {
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
    void getIndustryArticleByDateTest() {
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
    void getIndustryArticleByDateRangeTest() {
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
    void getLatestIndustryArticlesTest() {
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
    void getIndustryArticleByNumberTest() {
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
    void getIndustryArticleByNameTest() {
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
    void getIndustryArticleByLinkTest() {
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

    @DisplayName("비어 있는 산업 기사 획득")
    @Test
    void getEmptyIndustryArticleTest() {
        // given & when
        IndustryArticle article = IndustryArticle.builder().article(testIndustryArticle).number(1L).build();

        // then
        for (Optional<IndustryArticle> emptyArticle : List.of(
                articleRepository.getArticleByNumber(article.getNumber()),
                articleRepository.getArticleByName(article.getName()),
                articleRepository.getArticleByLink(article.getLink()))) {
            assertThat(emptyArticle).isEmpty();
        }
    }

    @DisplayName("산업 기사 저장")
    @Test
    void saveIndustryArticleTest() {
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
    void updateIndustryArticleTest() {
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
    void removeIndustryArticleByNameTest() {
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