package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.Post;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.PostEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.PostEntityTestUtils;
import site.hixview.support.spring.util.PostTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.POST;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class PostEntityMapperTest implements PostTestUtils, PostEntityTestUtils {

    private final PostEntityRepository postEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final PostEntityMapperImpl mapperImpl = new PostEntityMapperImpl();

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + POST};

    @Autowired
    PostEntityMapperTest(PostEntityRepository postEntityRepository, JdbcTemplate jdbcTemplate) {
        this.postEntityRepository = postEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 Post 일관성 보장")
    @Test
    void postMappingWithEntityMapper() {
        // given
        PostEntity postEntity = postEntityRepository.save(createPostEntity());

        // when
        Post post = Post.builder().number(postEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toPost(
                mapperImpl.toPostEntity(post))).isEqualTo(post);
    }

    @DisplayName("엔터티 매퍼 사용 후 PostEntity 일관성 보장")
    @Test
    void postEntityMappingWithEntityMapper() {
        // given & when
        PostEntity postEntity = postEntityRepository.save(createPostEntity());

        // then
        assertThat(mapperImpl.toPostEntity(
                mapperImpl.toPost(postEntity))).isEqualTo(postEntity);
    }
}