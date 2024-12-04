package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.EconomyArticleTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.support.jpa.util.ObjectTestUtils.resetAutoIncrement;

@OnlyRealRepositoryContext
class EconomyArticleRepositoryTest implements EconomyArticleTestUtils {

    @Autowired
    private EconomyArticleRepository economyArticleRepository;

    private static final Logger log = LoggerFactory.getLogger(EconomyArticleRepositoryTest.class);

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        resetAutoIncrement(applicationContext);
    }

    @DisplayName("JpaRepository 경제 기사 연결")
    @Test
    void connectTest() {
        // given
        EconomyArticleEntity article = createEconomyArticle();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("날짜로 경제 기사 찾기")
    @Test
    void findByDateTest() {
        // given
        EconomyArticleEntity article = createEconomyArticle();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByDate(article.getDate())).isEqualTo(List.of(article));
    }

    @DisplayName("날짜 범위로 경제 기사 찾기")
    @Test
    void findByDateBetweenTest() {
        // given
        EconomyArticleEntity articleFirst = createEconomyArticle();
        EconomyArticleEntity articleLast = createAnotherEconomyArticle();
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
        EconomyArticleEntity article = createEconomyArticle();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findBySubjectCountry(article.getSubjectCountry())).isEqualTo(List.of(article));
    }

    @DisplayName("중요성으로 경제 기사 찾기")
    @Test
    void findByImportanceTest() {
        // given
        EconomyArticleEntity article = createEconomyArticle();
        EconomyArticleEntity anotherEconomyArticle = createAnotherEconomyArticle();
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
        EconomyArticleEntity article = createEconomyArticle();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이름으로 경제 기사 찾기")
    @Test
    void findByNameTest() {
        // given
        EconomyArticleEntity article = createEconomyArticle();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByName(article.getName()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("링크로 경제 기사 찾기")
    @Test
    void findByLinkTest() {
        // given
        EconomyArticleEntity article = createEconomyArticle();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.findByLink(article.getLink()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("번호로 경제 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        EconomyArticleEntity article = createEconomyArticle();
        economyArticleRepository.save(article);

        // when
        economyArticleRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(economyArticleRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 기사 확인")
    @Test
    void existsByNumberTest() {
        // given
        EconomyArticleEntity article = createEconomyArticle();

        // when
        economyArticleRepository.save(article);

        // then
        assertThat(economyArticleRepository.existsByNumber(article.getNumber())).isEqualTo(true);
    }
}