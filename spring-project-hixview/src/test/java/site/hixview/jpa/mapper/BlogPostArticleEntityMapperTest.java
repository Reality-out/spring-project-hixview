package site.hixview.jpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.BlogPostArticleEntityTestUtils;
import site.hixview.support.spring.util.BlogPostArticleTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.POST;
import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_ARTI_MAPPER_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
@Slf4j
class BlogPostArticleEntityMapperTest implements BlogPostArticleTestUtils, BlogPostArticleEntityTestUtils {

    private final BlogPostArticleEntityRepository blogPostArticleEntityRepository;
    private final BlogPostEntityRepository blogPostEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + BLOG_POST_ARTI_MAPPER_SNAKE,
            TEST_TABLE_PREFIX + BLOG_POST_SNAKE, TEST_TABLE_PREFIX + POST, TEST_TABLE_PREFIX + ARTICLE};

    private final BlogPostArticleEntityMapperImpl mapperImpl = new BlogPostArticleEntityMapperImpl();

    @Autowired
    BlogPostArticleEntityMapperTest(BlogPostArticleEntityRepository blogPostArticleEntityRepository, BlogPostEntityRepository blogPostEntityRepository, ArticleEntityRepository articleEntityRepository, JdbcTemplate jdbcTemplate) {
        this.blogPostArticleEntityRepository = blogPostArticleEntityRepository;
        this.blogPostEntityRepository = blogPostEntityRepository;
        this.articleEntityRepository = articleEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 BlogPostArticle 일관성 보장")
    @Test
    void blogPostArticleMappingWithEntityMapper() {
        // given
        PostEntity postEntity = createPostEntity();
        BlogPostEntity blogPostEntity = BlogPostEntity.builder().blogPost(createBlogPostEntity()).post(postEntity).build();
        ArticleEntity articleEntity = new ArticleEntity();

        // when
        BlogPostArticleEntity blogPostArticleEntity = blogPostArticleEntityRepository.save(new BlogPostArticleEntity(blogPostEntity, articleEntity));
        BlogPostArticle blogPostArticle = BlogPostArticle.builder()
                .number(blogPostArticleEntity.getNumber())
                .postNumber(blogPostArticleEntity.getBlogPost().getNumber())
                .articleNumber(blogPostArticleEntity.getArticle().getNumber())
                .build();

        // then
        assertThat(mapperImpl.toBlogPostArticle(mapperImpl.toBlogPostArticleEntity(blogPostArticle,
                blogPostEntityRepository, articleEntityRepository))).usingRecursiveComparison().isEqualTo(blogPostArticle);
    }

    @DisplayName("엔터티 매퍼 사용 후 BlogPostArticleEntity 일관성 보장")
    @Test
    void blogPostArticleEntityMappingWithEntityMapper() {
        // given
        PostEntity postEntity = createPostEntity();
        BlogPostEntity blogPostEntity = BlogPostEntity.builder().blogPost(createBlogPostEntity()).post(postEntity).build();
        ArticleEntity articleEntity = new ArticleEntity();

        // when
        BlogPostArticleEntity blogPostArticleEntity = blogPostArticleEntityRepository.save(new BlogPostArticleEntity(blogPostEntity, articleEntity));

        // then
        assertThat(mapperImpl.toBlogPostArticleEntity(mapperImpl.toBlogPostArticle(blogPostArticleEntity),
                blogPostEntityRepository, articleEntityRepository)).isEqualTo(blogPostArticleEntity);
    }
}