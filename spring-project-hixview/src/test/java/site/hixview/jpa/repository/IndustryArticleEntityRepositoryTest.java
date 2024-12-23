package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.IndustryArticleEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.aggregate.vo.WordSnake.INDUSTRY_ARTICLE_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class IndustryArticleEntityRepositoryTest implements IndustryArticleEntityTestUtils {

    private final IndustryArticleEntityRepository industryArticleRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + INDUSTRY_ARTICLE_SNAKE,
            TEST_TABLE_PREFIX + ARTICLE, TEST_TABLE_PREFIX + PRESS};

    @Autowired
    IndustryArticleEntityRepositoryTest(IndustryArticleEntityRepository industryArticleRepository, JdbcTemplate jdbcTemplate) {
        this.industryArticleRepository = industryArticleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("날짜로 산업 기사 찾기")
    @Test
    void findByDateTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByDate(article.getDate())).isEqualTo(List.of(article));
    }

    @DisplayName("날짜 범위로 산업 기사 찾기")
    @Test
    void findByDateBetweenTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();
        List<IndustryArticleEntity> articleList = List.of(article);

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByDateBetween(article.getDate(), article.getDate())).isEqualTo(articleList);
        assertThat(industryArticleRepository.findByDateBetween(article.getDate().minusDays(10), article.getDate().plusDays(10))).isEqualTo(articleList);
    }

    @DisplayName("대상 국가로 산업 기사 찾기")
    @Test
    void findBySubjectCountryTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findBySubjectCountry(article.getSubjectCountry())).isEqualTo(List.of(article));
    }

    @DisplayName("중요도로 산업 기사 찾기")
    @Test
    void findByImportanceTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();
        List<IndustryArticleEntity> articleList = List.of(article);

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByImportance(article.getImportance())).isEqualTo(articleList);
    }

    @DisplayName("언론사로 산업 기사 찾기")
    @Test
    void findByPressTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();
        List<IndustryArticleEntity> articleList = List.of(article);

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByPress(article.getPress())).isEqualTo(articleList);
    }

    @DisplayName("번호로 산업 기사 찾기")
    @Test
    void findByNumberTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("이름으로 산업 기사 찾기")
    @Test
    void findByNameTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByName(article.getName()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("링크로 산업 기사 찾기")
    @Test
    void findByLinkTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByLink(article.getLink()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("1차 업종으로 산업 기사 찾기")
    @Test
    void findByFirstCategoryTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();
        List<IndustryArticleEntity> articleList = List.of(article);

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.findByFirstCategory(article.getFirstCategory())).isEqualTo(articleList);
    }

    @DisplayName("번호로 산업 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        IndustryArticleEntity article = industryArticleRepository.save(createIndustryArticleEntity());

        // when
        industryArticleRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(industryArticleRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 기사 확인")
    @Test
    void existsByNumberTest() {
        // given
        IndustryArticleEntity article = createIndustryArticleEntity();

        // when
        industryArticleRepository.save(article);

        // then
        assertThat(industryArticleRepository.existsByNumber(article.getNumber())).isEqualTo(true);
    }
}