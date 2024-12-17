package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.BlogPostArticleEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class BlogPostArticleEntityRepositoryTest implements BlogPostArticleEntityTestUtils {

    private final BlogPostArticleEntityRepository blogPostMapperRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    BlogPostArticleEntityRepositoryTest(BlogPostArticleEntityRepository blogPostMapperRepository, JdbcTemplate jdbcTemplate) {
        this.blogPostMapperRepository = blogPostMapperRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticleEntity();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.findByNumber(mapper.getNumber()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("블로그 포스트로 블로그 포스트와 기사 간 매퍼 찾기")
    @Test
    void findByBlogPostTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticleEntity();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.findByBlogPost(mapper.getBlogPost())).isEqualTo(List.of(mapper));
    }

    @DisplayName("기사로 블로그 포스트와 기사 간 매퍼 찾기")
    @Test
    void findByArticleTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticleEntity();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.findByArticle(mapper.getArticle())).isEqualTo(List.of(mapper));
    }

    @DisplayName("블로그 포스트와 기사로 블로그 포스트와 기사 간 매퍼 찾기")
    @Test
    void findByBlogPostAndArticleTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticleEntity();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.findByBlogPostAndArticle(
                mapper.getBlogPost(), mapper.getArticle()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        BlogPostArticleEntity mapper = blogPostMapperRepository.save(createBlogPostArticleEntity());

        // when
        blogPostMapperRepository.deleteByNumber(mapper.getNumber());

        // then
        assertThat(blogPostMapperRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 확인")
    @Test
    void existsByNumberTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticleEntity();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}