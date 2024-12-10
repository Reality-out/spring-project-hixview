package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.executor.SqlExecutor;
import site.hixview.support.jpa.util.ArticleTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class ArticleRepositoryTest implements ArticleTestUtils {

    @Autowired
    private ArticleRepository articleRepository;

    private static final Logger log = LoggerFactory.getLogger(ArticleRepositoryTest.class);

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        new SqlExecutor().resetAutoIncrement(applicationContext);
    }

    @DisplayName("JpaRepository 기사 연결")
    @Test
    void connectTest() {
        // given
        ArticleEntity article = createArticle();

        // when
        articleRepository.save(article);

        // then
        assertThat(articleRepository.findByNumber(article.getNumber()).orElseThrow()).isEqualTo(article);
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