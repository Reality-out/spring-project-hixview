package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.repository.BlogPostRepository;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.util.BlogPostTestUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class BlogPostRepositoryImplTest implements BlogPostTestUtils {

    @Autowired
    private BlogPostRepository postRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    BlogPostRepositoryImplTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_BLOG_POSTS_SCHEMA, true);
    }

    @DisplayName("블로그 포스트들 획득")
    @Test
    void getBlogPostsTest() {
        // given
        BlogPost post1 = testBlogPostCompany;
        BlogPost post2 = testBlogPostEconomy;

        // when
        post1 = BlogPost.builder().post(post1).number(postRepository.savePost(post1)).build();
        post2 = BlogPost.builder().post(post2).number(postRepository.savePost(post2)).build();

        // then
        assertThat(postRepository.getPosts()).isEqualTo(List.of(post1, post2));
    }

    @DisplayName("최신 블로그 포스트들 획득")
    @Test
    void getLatestBlogPostsTest() {
        // given
        BlogPost post1 = testBlogPostCompany;
        BlogPost post2 = testBlogPostIndustry;
        BlogPost post3 = testBlogPostEconomy;

        // when
        post1 = BlogPost.builder().post(post1).number(postRepository.savePost(post1)).build();
        post2 = BlogPost.builder().post(post2).number(postRepository.savePost(post2)).build();
        post3 = BlogPost.builder().post(post3).number(postRepository.savePost(post3)).build();

        // then
        assertThat(postRepository.getLatestPosts(Classification.COMPANY)).isEqualTo(List.of(post1));
        assertThat(postRepository.getLatestPosts(Classification.INDUSTRY)).isEqualTo(List.of(post2));
        assertThat(postRepository.getLatestPosts(Classification.ECONOMY)).isEqualTo(List.of(post3));
    }

    @DisplayName("번호로 블로그 포스트 획득")
    @Test
    void getBlogPostByNumberTest() {
        // given
        BlogPost post = testBlogPostCompany;

        // when
        post = BlogPost.builder().post(post).number(postRepository.savePost(post)).build();

        // then
        assertThat(postRepository.getPostByNumber(post.getNumber()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("이름으로 블로그 포스트 획득")
    @Test
    void getBlogPostByNameTest() {
        // given
        BlogPost post = testBlogPostCompany;

        // when
        post = BlogPost.builder().post(post).number(postRepository.savePost(post)).build();

        // then
        assertThat(postRepository.getPostByName(post.getName()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("링크로 블로그 포스트 획득")
    @Test
    void getBlogPostByLinkTest() {
        // given
        BlogPost post = testBlogPostCompany;

        // when
        post = BlogPost.builder().post(post).number(postRepository.savePost(post)).build();

        // then
        assertThat(postRepository.getPostByLink(post.getLink()).orElseThrow()).isEqualTo(post);
    }

    @DisplayName("비어 있는 블로그 포스트 획득")
    @Test
    void getEmptyBlogPostTest() {
        // given & when
        BlogPost post = BlogPost.builder().post(testBlogPostCompany).number(1L).build();

        // then
        for (Optional<BlogPost> emptyPost : List.of(
                postRepository.getPostByNumber(post.getNumber()),
                postRepository.getPostByName(post.getName()),
                postRepository.getPostByLink(post.getLink()))) {
            assertThat(emptyPost).isEmpty();
        }
    }

    @DisplayName("블로그 포스트 저장")
    @Test
    void saveBlogPostTest() {
        // given
        BlogPost post1 = testBlogPostCompany;
        BlogPost post2 = testBlogPostEconomy;

        // when
        post1 = BlogPost.builder().post(post1).number(postRepository.savePost(post1)).build();
        post2 = BlogPost.builder().post(post2).number(postRepository.savePost(post2)).build();

        // then
        assertThat(postRepository.getPosts()).isEqualTo(List.of(post1, post2));
    }

    @DisplayName("블로그 포스트 갱신")
    @Test
    void updateBlogPostTest() {
        // given
        BlogPost post1 = testBlogPostCompany;
        String commonName = post1.getName();
        String commonLink = post1.getLink();
        BlogPost post2 = BlogPost.builder().post(testBlogPostEconomy).name(commonName).link(commonLink).number(postRepository.savePost(post1)).build();

        // when
        postRepository.updatePost(post2);

        // then
        assertThat(postRepository.getPostByName(commonName).orElseThrow()).isEqualTo(post2);
    }

    @DisplayName("블로그 포스트 삭제")
    @Test
    void deleteBlogPostTest() {
        // given
        BlogPost post = testBlogPostCompany;
        postRepository.savePost(post);

        // when
        postRepository.deletePostByName(post.getName());

        // then
        assertThat(postRepository.getPosts()).isEmpty();
    }
}