package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.EconomyArticleEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_ARTICLE_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class EconomyArticleEntityRepositoryTest implements EconomyArticleEntityTestUtils {

    private final EconomyArticleEntityRepository economyArticleRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ECONOMY_ARTICLE_SNAKE,
            TEST_TABLE_PREFIX + ARTICLE, TEST_TABLE_PREFIX + PRESS};

    private static final Logger log = LoggerFactory.getLogger(EconomyArticleEntityRepositoryTest.class);

    @Autowired
    EconomyArticleEntityRepositoryTest(EconomyArticleEntityRepository economyArticleRepository, JdbcTemplate jdbcTemplate) {
        this.economyArticleRepository = economyArticleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("날짜로 경제 기사 찾기")
    @Test
    void findByDateTest() {
        // given
        EconomyArticleEntity article = createEconomyArticleEntity();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByDate(article.getDate())).isEqualTo(List.of(article));
    }

    @DisplayName("날짜 범위로 경제 기사 찾기")
    @Test
    void findByDateBetweenTest() {
        // given
        EconomyArticleEntity articleFirst = createEconomyArticleEntity();
        EconomyArticleEntity articleLast = createAnotherEconomyArticleEntity();
        List<EconomyArticleEntity> articleList = List.of(articleFirst, articleLast);

        // when
        economyArticleRepository.saveAll(articleList);

        // then
        assertThat(economyArticleRepository.findByDateBetween(articleFirst.getDate(), articleLast.getDate())).isEqualTo(articleList);
    }

    @DisplayName("대상 국가로 경제 기사 찾기")
    @Test
    void findBySubjectCountryTest() {
        // given
        EconomyArticleEntity article = createEconomyArticleEntity();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findBySubjectCountry(article.getSubjectCountry())).isEqualTo(List.of(article));
    }

    @DisplayName("중요성으로 경제 기사 찾기")
    @Test
    void findByImportanceTest() {
        // given
        EconomyArticleEntity article = createEconomyArticleEntity();
        EconomyArticleEntity anotherEconomyArticle = createAnotherEconomyArticleEntity();
        List<EconomyArticleEntity> articleList = List.of(article, anotherEconomyArticle);

        // when
        economyArticleRepository.saveAll(articleList);

        // then
        assertThat(economyArticleRepository.findByImportance(article.getImportance())).isEqualTo(articleList);
    }

    @DisplayName("번호로 경제 기사 찾기")
    @Test
    void findByNumberTest() {
        // given
        EconomyArticleEntity article = createEconomyArticleEntity();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이름으로 경제 기사 찾기")
    @Test
    void findByNameTest() {
        // given
        EconomyArticleEntity article = createEconomyArticleEntity();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByName(article.getName()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("링크로 경제 기사 찾기")
    @Test
    void findByLinkTest() {
        // given
        EconomyArticleEntity article = createEconomyArticleEntity();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByLink(article.getLink()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("번호로 경제 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        EconomyArticleEntity article = economyArticleRepository.save(createEconomyArticleEntity());

        // when
        economyArticleRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(economyArticleRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 기사 확인")
    @Test
    void existsByNumberTest() {
        // given
        EconomyArticleEntity article = createEconomyArticleEntity();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.existsByNumber(article.getNumber())).isEqualTo(true);
    }
}