package site.hixview.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.repository.BlogPostRepository;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.util.BlogPostTestUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.domain.entity.home.BlogPost.getFieldNamesWithNoNumber;
import static site.hixview.domain.vo.Word.NUMBER;

@OnlyRealRepositoryContext
class BlogPostRepositoryImplTest implements BlogPostTestUtils {

    @Autowired
    private BlogPostRepository postRepository;

    private final JdbcTemplate jdbcTemplateTest;
    private final String[] fieldNames = getFieldNamesWithNoNumber();

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
        postRepository.savePost(post1);
        postRepository.savePost(post2);

        // then
        assertThat(postRepository.getPosts())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(post1, post2));
    }

    @DisplayName("번호로 블로그 포스트 획득")
    @Test
    void getBlogPostByNumberTest() {
        // given
        BlogPost post = testBlogPostCompany;

        // when
        post = BlogPost.builder().blogPost(post).number(postRepository.savePost(post)).build();

        // then
        assertThat(postRepository.getPostByNumber(post.getNumber()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(post);
    }

    @DisplayName("이름으로 블로그 포스트 획득")
    @Test
    void getBlogPostByNameTest() {
        // given
        BlogPost post = testBlogPostCompany;

        // when
        postRepository.savePost(post);

        // then
        assertThat(postRepository.getPostByName(post.getName()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .comparingOnlyFields(fieldNames)
                .isEqualTo(post);
    }

    @DisplayName("링크로 블로그 포스트 획득")
    @Test
    void getBlogPostByLinkTest() {
        // given
        BlogPost post = testBlogPostCompany;

        // when
        postRepository.savePost(post);

        // then
        assertThat(postRepository.getPostByLink(post.getLink()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .comparingOnlyFields(fieldNames)
                .isEqualTo(post);
    }

    @DisplayName("비어 있는 블로그 포스트 획득")
    @Test
    void getEmptyBlogPostTest() {
        // given & when
        BlogPost post = BlogPost.builder().blogPost(testBlogPostCompany).number(1L).build();

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
        postRepository.savePost(post1);
        postRepository.savePost(post2);

        // then
        assertThat(postRepository.getPosts())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .comparingOnlyFields(fieldNames)
                .isEqualTo(List.of(post1, post2));
    }

    @DisplayName("블로그 포스트 갱신")
    @Test
    void updateBlogPostTest() {
        // given
        BlogPostDto post1 = createTestBlogPostCompanyDto();
        String commonName = post1.getName();
        String commonLink = post1.getLink();
        BlogPostDto post2 = createTestBlogPostEconomyDto();
        post2.setName(commonName);
        post2.setLink(commonLink);

        // when
        postRepository.savePost(BlogPost.builder().blogPostDto(post1).build());
        BlogPost blogPost2 = BlogPost.builder().blogPostDto(post2).build();
        postRepository.updatePost(blogPost2);

        // then
        assertThat(postRepository.getPostByName(commonName).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .comparingOnlyFields(fieldNames)
                .isEqualTo(blogPost2);
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