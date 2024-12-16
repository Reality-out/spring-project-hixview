package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.ArticleEntityTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class ArticleEntityRepositoryTest implements ArticleEntityTestUtils {

    private final ArticleEntityRepository articleEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ARTICLE};

    @Autowired
    ArticleEntityRepositoryTest(ArticleEntityRepository articleEntityRepository, JdbcTemplate jdbcTemplate) {
        this.articleEntityRepository = articleEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 기사 찾기")
    @Test
    void findByNumberTest() {
        // given
        ArticleEntity article = createArticleEntity();

        // when
        articleEntityRepository.save(article);

        // then
        assertThat(articleEntityRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
    }

    @DisplayName("번호로 기사 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        ArticleEntity article = articleEntityRepository.save(createArticleEntity());

        // when
        articleEntityRepository.deleteByNumber(article.getNumber());

        // then
        assertThat(articleEntityRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 기사 확인")
    @Test
    void existsByNumberTest() {
        // given
        ArticleEntity article = createArticleEntity();

        // when
        articleEntityRepository.save(article);

        // then
        assertThat(articleEntityRepository.existsByNumber(article.getNumber())).isEqualTo(true);
    }
}