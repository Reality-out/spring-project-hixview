package site.hixview.jpa.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.support.jpa.util.ArticleEntityTestUtils;
import site.hixview.support.spring.util.ArticleTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class ArticleEntityMapperTest implements ArticleEntityTestUtils, ArticleTestUtils {

    private final ArticleEntityMapperImpl mapperImpl = new ArticleEntityMapperImpl();

    @DisplayName("엔터티 매퍼 사용 후 Article 일관성 보장")
    @Test
    void articleMappingWithEntityMapper() {
        assertThat(mapperImpl.toArticle(mapperImpl.toArticleEntity(article))).isEqualTo(article);
    }

    @DisplayName("엔터티 매퍼 사용 후 ArticleEntity 일관성 보장")
    @Test
    void articleEntityMappingWithEntityMapper() {
        ArticleEntity articleEntity = createArticleEntity();
        assertThat(mapperImpl.toArticleEntity(mapperImpl.toArticle(articleEntity))).isEqualTo(articleEntity);
    }
}