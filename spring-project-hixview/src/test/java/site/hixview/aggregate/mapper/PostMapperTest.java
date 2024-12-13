package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.PostDto;
import site.hixview.support.spring.util.PostTestUtils;
import site.hixview.support.spring.util.dto.PostDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class PostMapperTest implements PostTestUtils, PostDtoTestUtils {

    private final PostMapperImpl mapperImpl = new PostMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(PostMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 Post 일관성 보장")
    @Test
    void postMappingWithDomainMapper() {
        assertThat(mapperImpl.toPost(mapperImpl.toPostDto(post))).isEqualTo(post);
    }

    @DisplayName("도메인 매퍼 사용 후 PostDto 일관성 보장")
    @Test
    void postDtoMappingWithDomainMapper() {
        PostDto postDto = createPostDto();
        assertThat(mapperImpl.toPostDto(mapperImpl.toPost(postDto))).usingRecursiveComparison().isEqualTo(postDto);
    }
}