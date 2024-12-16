package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.ArticleEntityTestUtils;
import site.hixview.support.jpa.util.BlogPostEntityTestUtils;
import site.hixview.support.spring.util.BlogPostTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.POST;
import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_ARTI_MAPPER_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class BlogPostEntityMapperTest implements BlogPostTestUtils, BlogPostEntityTestUtils, ArticleEntityTestUtils {

    private final ArticleEntityRepository articleEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final BlogPostEntityRepository blogPostEntityRepository;
    private final BlogPostArticleEntityRepository blogPostArticleEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + BLOG_POST_ARTI_MAPPER_SNAKE,
            TEST_TABLE_PREFIX + BLOG_POST_SNAKE, TEST_TABLE_PREFIX + POST, TEST_TABLE_PREFIX + ARTICLE};

    private final BlogPostEntityMapperImpl mapperImpl = new BlogPostEntityMapperImpl();

    @Autowired
    BlogPostEntityMapperTest(ArticleEntityRepository articleEntityRepository, BlogPostEntityRepository blogPostEntityRepository, PostEntityRepository postEntityRepository, BlogPostArticleEntityRepository blogPostArticleEntityRepository, JdbcTemplate jdbcTemplate) {
        this.articleEntityRepository = articleEntityRepository;
        this.blogPostEntityRepository = blogPostEntityRepository;
        this.postEntityRepository = postEntityRepository;
        this.blogPostArticleEntityRepository = blogPostArticleEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 BlogPost 일관성 보장")
    @Test
    void blogPostMappingWithEntityMapper() {
        // given
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        ArticleEntity anotherArticleEntity = articleEntityRepository.save(createAnotherArticleEntity());
        PostEntity postEntity = postEntityRepository.save(createPostEntity());
        BlogPost testBlogPost = BlogPost.builder().blogPost(blogPost).number(postEntity.getNumber())
                .mappedArticleNumbers(List.of(articleEntity.getNumber(), anotherArticleEntity.getNumber())).build();
        BlogPostEntity blogPostEntity = blogPostEntityRepository.save(BlogPostEntity.builder()
                .blogPost(createBlogPostEntity()).post(postEntity).build());
        BlogPostArticleEntity blogPostArticleEntity = blogPostArticleEntityRepository.save(
                new BlogPostArticleEntity(blogPostEntity, articleEntity));
        BlogPostArticleEntity anotherBlogPostArticleEntity = blogPostArticleEntityRepository.save(
                new BlogPostArticleEntity(blogPostEntity, anotherArticleEntity));

        // when
        BlogPostEntity mappedBlogPostEntity = mapperImpl.toBlogPostEntity(testBlogPost, postEntityRepository);

        // then
        assertThat(mappedBlogPostEntity).isEqualTo(blogPostEntity);
        assertThat(mapperImpl.toBlogPost(blogPostEntity, blogPostArticleEntityRepository))
                .usingRecursiveComparison().isEqualTo(testBlogPost);
    }

    @DisplayName("엔터티 매퍼 사용 후 BlogPostEntity 일관성 보장")
    @Test
    void blogPostEntityMappingWithEntityMapper() {
        // given & when
        ArticleEntity articleEntity = articleEntityRepository.save(createArticleEntity());
        ArticleEntity anotherArticleEntity = articleEntityRepository.save(createAnotherArticleEntity());
        PostEntity postEntity = postEntityRepository.save(createPostEntity());
        BlogPost testBlogPost = BlogPost.builder().blogPost(blogPost).number(postEntity.getNumber())
                .mappedArticleNumbers(List.of(articleEntity.getNumber(), anotherArticleEntity.getNumber())).build();
        BlogPostEntity blogPostEntity = blogPostEntityRepository.save(BlogPostEntity.builder()
                .blogPost(createBlogPostEntity()).post(postEntity).build());
        BlogPostArticleEntity blogPostArticleEntity = blogPostArticleEntityRepository.save(
                new BlogPostArticleEntity(blogPostEntity, articleEntity));
        BlogPostArticleEntity anotherBlogPostArticleEntity = blogPostArticleEntityRepository.save(
                new BlogPostArticleEntity(blogPostEntity, anotherArticleEntity));

        // then
        assertThat(mapperImpl.toBlogPostEntity(mapperImpl.toBlogPost(blogPostEntity, blogPostArticleEntityRepository),
                postEntityRepository)).isEqualTo(blogPostEntity);
    }
}