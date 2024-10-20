package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.repository.EconomyArticleRepository;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.util.EconomyArticleTestUtils;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.domain.vo.Word.NAME;

@OnlyRealRepositoryContext
class EconomyArticleRepositoryImplTest implements EconomyArticleTestUtils {

    @Autowired
    private EconomyArticleRepository articleRepository;

    private final JdbcTemplate jdbcTemplateTest;
    private final String[] fieldNames = EconomyArticle.getFieldNamesWithNoNumber();

    @Autowired
    EconomyArticleRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ECONOMY_ARTICLES_SCHEMA, true);
    }

    @DisplayName("경제 기사들 획득")
    @Test
    void getEconomyArticlesTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testNewEconomyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticles())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("경제 기사들 날짜로 획득")
    @Test
    void getEconomyArticleByDateTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testEqualDateEconomyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticlesByDate(article1.getDate()))
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("경제 기사들 날짜 범위로 획득")
    @Test
    void getEconomyArticleByDateRangeTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testEqualDateEconomyArticle;
        EconomyArticle article3 = testNewEconomyArticle;

        List<EconomyArticle> articles = List.of(article1, article2, article3);
        List<EconomyArticle> sortedArticles = articles.stream()
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
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(article1, article2, article3));
    }

    @DisplayName("최신 경제 기사들 획득")
    @Test
    void getLatestEconomyArticlesTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testEqualDateEconomyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);
        articleRepository.saveArticle(testNewEconomyArticle);

        // then
        assertThat(articleRepository.getLatestArticles())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(article1, article2));
    }

    @DisplayName("번호로 경제 기사 획득")
    @Test
    void getEconomyArticleByNumberTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testNewEconomyArticle;

        // when
        article1 = EconomyArticle.builder().article(article1).number(articleRepository.saveArticle(article1)).build();
        article2 = EconomyArticle.builder().article(article2).number(articleRepository.saveArticle(article2)).build();

        // then
        assertThat(articleRepository.getArticleByNumber(article1.getNumber()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByNumber(article2.getNumber()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article2);
    }

    @DisplayName("경제 기사 이름으로 획득")
    @Test
    void getEconomyArticleByNameTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testNewEconomyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticleByName(article1.getName()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByName(article2.getName()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article2);
    }

    @DisplayName("경제 기사 링크로 획득")
    @Test
    void getEconomyArticleByLinkTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testNewEconomyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        assertThat(articleRepository.getArticleByLink(article1.getLink()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article1);

        assertThat(articleRepository.getArticleByLink(article2.getLink()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article2);
    }

    @DisplayName("비어 있는 경제 기사 획득")
    @Test
    void getEmptyEconomyArticleTest() {
        // given & when
        EconomyArticle article = EconomyArticle.builder().article(testEconomyArticle).number(1L).build();

        // then
        for (Optional<EconomyArticle> emptyArticle : List.of(
                articleRepository.getArticleByNumber(article.getNumber()),
                articleRepository.getArticleByName(article.getName()),
                articleRepository.getArticleByLink(article.getLink()))) {
            assertThat(emptyArticle).isEmpty();
        }
    }

    @DisplayName("경제 기사 저장")
    @Test
    void saveEconomyArticleTest() {
        // given
        EconomyArticle article = testEconomyArticle;

        // when
        articleRepository.saveArticle(article);

        // then
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article);
    }

    @DisplayName("경제 기사 갱신")
    @Test
    void updateEconomyArticleTest() {
        // given
        EconomyArticle article = testEconomyArticle;

        // when
        articleRepository.saveArticle(article);

        // then
        articleRepository.updateArticle(EconomyArticle.builder().article(testNewEconomyArticle).name(article.getName()).build());
        assertThat(articleRepository.getArticleByName(article.getName()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .ignoringFields(NAME)
                .isEqualTo(testNewEconomyArticle);
    }

    @DisplayName("경제 기사 이름으로 제거")
    @Test
    void removeEconomyArticleByNameTest() {
        // given
        EconomyArticle article1 = testEconomyArticle;
        EconomyArticle article2 = testEqualDateEconomyArticle;

        // when
        articleRepository.saveArticle(article1);
        articleRepository.saveArticle(article2);

        // then
        articleRepository.deleteArticleByName(article1.getName());
        articleRepository.deleteArticleByName(article2.getName());
        assertThat(articleRepository.getArticles()).isEmpty();
    }
}