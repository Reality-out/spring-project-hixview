package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.support.spring.util.BlogPostTestUtils;
import site.hixview.support.spring.util.dto.BlogPostDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class BlogPostMapperTest implements BlogPostTestUtils, BlogPostDtoTestUtils {

    private final BlogPostMapperImpl mapperImpl = new BlogPostMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(BlogPostMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 BlogPost 일관성 보장")
    @Test
    void blogPostMappingWithDomainMapper() {
        assertThat(mapperImpl.toBlogPost(mapperImpl.toBlogPostDto(blogPost))).isEqualTo(blogPost);
    }

    @DisplayName("도메인 매퍼 사용 후 BlogPostDto 일관성 보장")
    @Test
    void blogPostDtoMappingWithDomainMapper() {
        BlogPostDto BlogPostDto = createBlogPostDto();
        assertThat(mapperImpl.toBlogPostDto(mapperImpl.toBlogPost(BlogPostDto))).usingRecursiveComparison().isEqualTo(BlogPostDto);
    }
}