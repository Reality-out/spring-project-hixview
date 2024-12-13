package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.ArticleDto;
import site.hixview.support.spring.util.ArticleTestUtils;
import site.hixview.support.spring.util.dto.ArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class ArticleMapperTest implements ArticleTestUtils, ArticleDtoTestUtils {

    private final ArticleMapperImpl mapperImpl = new ArticleMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(ArticleMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 Article 일관성 보장")
    @Test
    void articleMappingWithDomainMapper() {
        assertThat(mapperImpl.toArticle(mapperImpl.toArticleDto(article))).isEqualTo(article);
    }

    @DisplayName("도메인 매퍼 사용 후 ArticleDto 일관성 보장")
    @Test
    void articleDtoMappingWithDomainMapper() {
        ArticleDto articleDto = createArticleDto();
        assertThat(mapperImpl.toArticleDto(mapperImpl.toArticle(articleDto))).usingRecursiveComparison().isEqualTo(articleDto);
    }
}