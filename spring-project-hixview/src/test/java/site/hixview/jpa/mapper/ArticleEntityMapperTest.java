package site.hixview.jpa.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.ArticleEntityTestUtils;
import site.hixview.support.spring.util.ArticleTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class ArticleEntityMapperTest implements ArticleTestUtils, ArticleEntityTestUtils {

    private final ArticleEntityRepository articleEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final ArticleEntityMapperImpl mapperImpl = new ArticleEntityMapperImpl();

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + ARTICLE};

    @Autowired
    ArticleEntityMapperTest(ArticleEntityRepository articleEntityRepository, JdbcTemplate jdbcTemplate) {
        this.articleEntityRepository = articleEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 Article 일관성 보장")
    @Test
    void articleMappingWithEntityMapper() {
        // given
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());

        // when
        Article article = Article.builder().number(articleEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toArticle(
                mapperImpl.toArticleEntity(article))).isEqualTo(article);
    }

    @DisplayName("엔터티 매퍼 사용 후 ArticleEntity 일관성 보장")
    @Test
    void articleEntityMappingWithEntityMapper() {
        // given & when
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());

        // then
        assertThat(mapperImpl.toArticleEntity(
                mapperImpl.toArticle(articleEntity))).isEqualTo(articleEntity);
    }
}