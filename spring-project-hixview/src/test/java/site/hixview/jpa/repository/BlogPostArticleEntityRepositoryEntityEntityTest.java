package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.*;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.BlogPostArticleEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class BlogPostArticleEntityRepositoryEntityTest implements BlogPostArticleEntityTestUtils {

    @Autowired
    private BlogPostArticleEntityRepository blogPostMapperRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(BlogPostArticleEntityRepositoryEntityTest.class);

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticle();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.findByNumber(mapper.getNumber()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("블로그 포스트로 블로그 포스트와 기사 간 매퍼 찾기")
    @Test
    void findByBlogPostTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticle();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.findByBlogPost(mapper.getBlogPost())).isEqualTo(List.of(mapper));
    }

    @DisplayName("기사로 블로그 포스트와 기사 간 매퍼 찾기")
    @Test
    void findByArticleTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticle();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.findByArticle(mapper.getArticle())).isEqualTo(List.of(mapper));
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        BlogPostArticleEntity mapper = blogPostMapperRepository.save(createBlogPostArticle());

        // when
        blogPostMapperRepository.deleteByNumber(mapper.getNumber());

        // then
        assertThat(blogPostMapperRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 확인")
    @Test
    void existsByNumberTest() {
        // given
        BlogPostArticleEntity mapper = createBlogPostArticle();

        // when
        blogPostMapperRepository.save(mapper);

        // then
        assertThat(blogPostMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}