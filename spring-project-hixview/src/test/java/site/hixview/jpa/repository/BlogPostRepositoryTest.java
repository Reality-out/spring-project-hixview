package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.BlogPostTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.POST;
import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_SNAKE;
import static site.hixview.support.jpa.util.ObjectTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class BlogPostRepositoryTest implements BlogPostTestUtils {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + BLOG_POST_SNAKE, TEST_TABLE_PREFIX + POST};

    private static final Logger log = LoggerFactory.getLogger(BlogPostRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("날짜로 블로그 포스트 찾기")
    @Test
    void findByDateTest() {
        // given
        BlogPostEntity post = createBlogPost();

        // when
        blogPostRepository.save(post);

        // then
        assertThat(blogPostRepository.findByDate(post.getDate())).isEqualTo(List.of(post));
    }

    @DisplayName("날짜 범위로 블로그 포스트 찾기")
    @Test
    void findByDateBetweenTest() {
        // given
        BlogPostEntity postFirst = createBlogPost();
        BlogPostEntity postLast = createAnotherBlogPost();
        List<BlogPostEntity> postList = List.of(postFirst, postLast);

        // when
        blogPostRepository.saveAll(postList);

        // then
        assertThat(blogPostRepository.findByDateBetween(postFirst.getDate(), postLast.getDate())).isEqualTo(postList);
    }

    @DisplayName("분류로 블로그 포스트 찾기")
    @Test
    void findBySubjectCountryTest() {
        // given
        BlogPostEntity post = createBlogPost();

        // when
        blogPostRepository.save(post);

        // then
        assertThat(blogPostRepository.findByClassification(post.getClassification())).isEqualTo(List.of(post));
    }

    @DisplayName("번호로 블로그 포스트 찾기")
    @Test
    void findByNumberTest() {
        // given
        BlogPostEntity post = createBlogPost();

        // when
        blogPostRepository.save(post);

        // then
        assertThat(blogPostRepository.findByNumber(post.getNumber()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("이름으로 블로그 포스트 찾기")
    @Test
    void findByNameTest() {
        // given
        BlogPostEntity post = createBlogPost();

        // when
        blogPostRepository.save(post);

        // then
        assertThat(blogPostRepository.findByName(post.getName()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("링크로 블로그 포스트 찾기")
    @Test
    void findByLinkTest() {
        // given
        BlogPostEntity post = createBlogPost();

        // when
        blogPostRepository.save(post);

        // then
        assertThat(blogPostRepository.findByLink(post.getLink()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("번호로 블로그 포스트 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        BlogPostEntity post = blogPostRepository.save(createBlogPost());

        // when
        blogPostRepository.deleteByNumber(post.getNumber());

        // then
        assertThat(blogPostRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 블로그 포스트 확인")
    @Test
    void existsByNumberTest() {
        // given
        BlogPostEntity post = createBlogPost();

        // when
        blogPostRepository.save(post);

        // then
        assertThat(blogPostRepository.existsByNumber(post.getNumber())).isEqualTo(true);
    }
}