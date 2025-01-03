package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.PostEntityTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.POST;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class PostEntityRepositoryTest implements PostEntityTestUtils {

    private final PostEntityRepository postEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + POST};

    @Autowired
    PostEntityRepositoryTest(PostEntityRepository postEntityRepository, JdbcTemplate jdbcTemplate) {
        this.postEntityRepository = postEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("번호로 포스트 찾기")
    @Test
    void findByNumberTest() {
        // given
        PostEntity post = createPostEntity();

        // when
        postEntityRepository.save(post);

        // then
        assertThat(postEntityRepository.findByNumber(post.getNumber()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("번호로 포스트 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        PostEntity post = postEntityRepository.save(createPostEntity());

        // when
        postEntityRepository.deleteByNumber(post.getNumber());

        // then
        assertThat(postEntityRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 포스트 확인")
    @Test
    void existsByNumberTest() {
        // given
        PostEntity post = createPostEntity();

        // when
        postEntityRepository.save(post);

        // then
        assertThat(postEntityRepository.existsByNumber(post.getNumber())).isEqualTo(true);
    }
}