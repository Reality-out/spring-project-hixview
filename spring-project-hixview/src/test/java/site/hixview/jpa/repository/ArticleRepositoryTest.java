package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.ArticleTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.support.jpa.util.ObjectTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class ArticleRepositoryTest implements ArticleTestUtils {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ARTICLE};

    private static final Logger log = LoggerFactory.getLogger(ArticleRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 기사 찾기")
    @Test
    void findByNumberTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("번호로 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        ArticleEntity article = articleRepository.save(createArticle());

        // when
        articleRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(articleRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 기사 확인")
    @Test
    void existsByNumberTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.existsByNumber(article.getNumber())).isEqualTo(true);
    }
}