package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.BlogPostArticleDto;
import site.hixview.support.spring.util.BlogPostArticleTestUtils;
import site.hixview.support.spring.util.dto.BlogPostArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class BlogPostArticleMapperTest implements BlogPostArticleTestUtils, BlogPostArticleDtoTestUtils {

    private final BlogPostArticleMapper mapperImpl = new BlogPostArticleMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 BlogPostArticle 일관성 보장")
    @Test
    void blogPostArticleMappingWithDomainMapper() {
        assertThat(mapperImpl.toBlogPostArticle(mapperImpl.toBlogPostArticleDto(blogPostArticle))).isEqualTo(blogPostArticle);
    }

    @DisplayName("도메인 매퍼 사용 후 BlogPostArticleDto 일관성 보장")
    @Test
    void blogPostArticleDtoMappingWithDomainMapper() {
        BlogPostArticleDto BlogPostArticleDto = createBlogPostArticleDto();
        assertThat(mapperImpl.toBlogPostArticleDto(mapperImpl.toBlogPostArticle(BlogPostArticleDto))).usingRecursiveComparison().isEqualTo(BlogPostArticleDto);
    }
}