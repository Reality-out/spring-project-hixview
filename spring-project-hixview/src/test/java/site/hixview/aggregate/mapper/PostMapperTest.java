package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.PostDto;
import site.hixview.support.spring.util.PostTestUtils;
import site.hixview.support.spring.util.dto.PostDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class PostMapperTest implements PostTestUtils, PostDtoTestUtils {

    private final PostMapper mapperImpl = new PostMapperImpl();

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