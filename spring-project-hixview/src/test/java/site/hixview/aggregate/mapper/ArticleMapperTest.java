package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.ArticleDto;
import site.hixview.support.spring.util.ArticleTestUtils;
import site.hixview.support.spring.util.dto.ArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class ArticleMapperTest implements ArticleTestUtils, ArticleDtoTestUtils {

    private final ArticleMapper mapperImpl = new ArticleMapperImpl();

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