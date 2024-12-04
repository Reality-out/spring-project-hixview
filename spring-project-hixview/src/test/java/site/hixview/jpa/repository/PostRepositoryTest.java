package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.PostTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.support.jpa.util.ObjectTestUtils.resetAutoIncrement;

@OnlyRealRepositoryContext
class PostRepositoryTest implements PostTestUtils {

    @Autowired
    private PostRepository postRepository;

    private static final Logger log = LoggerFactory.getLogger(PostRepositoryTest.class);

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        resetAutoIncrement(applicationContext);
    }

    @DisplayName("JpaRepository 포스트 연결")
    @Test
    void connectTest() {
        // given
        PostEntity post = createPost();

        // when
        postRepository.save(post);

        // then
        assertThat(postRepository.findByNumber(post.getNumber()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("번호로 포스트 찾기")
    @Test
    void findByNumberTest() {
        // given
        PostEntity post = createPost();

        // when
        postRepository.save(post);

        // then
        assertThat(postRepository.findByNumber(post.getNumber()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("번호로 포스트 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        PostEntity post = postRepository.save(createPost());

        // when
        postRepository.deleteByNumber(post.getNumber());

        // then
        assertThat(postRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 포스트 확인")
    @Test
    void existsByNumberTest() {
        // given
        PostEntity post = createPost();

        // when
        postRepository.save(post);

        // then
        assertThat(postRepository.existsByNumber(post.getNumber())).isEqualTo(true);
    }
}