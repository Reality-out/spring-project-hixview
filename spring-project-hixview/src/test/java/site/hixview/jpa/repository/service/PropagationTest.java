package site.hixview.jpa.repository.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;
import site.hixview.jpa.service.BlogPostArticleEntityService;
import site.hixview.jpa.service.BlogPostEntityService;
import site.hixview.support.jpa.context.RealRepositoryAndServiceContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.BlogPostArticleEntityTestUtils;
import site.hixview.support.spring.util.BlogPostTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@RealRepositoryAndServiceContext
@Slf4j
class PropagationTest implements BlogPostArticleEntityTestUtils, BlogPostTestUtils {

    private final TestEntityManager entityManager;
    private final BlogPostEntityService bpEntityService;
    private final BlogPostArticleEntityService bpaEntityService;
    private final BlogPostEntityRepository bpEntityRepository;
    private final BlogPostArticleEntityRepository bpaEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PropagationTest(TestEntityManager entityManager, BlogPostEntityService bpEntityService, BlogPostArticleEntityService bpaEntityService, BlogPostEntityRepository bpEntityRepository, BlogPostArticleEntityRepository bpaEntityRepository, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.bpEntityService = bpEntityService;
        this.bpaEntityService = bpaEntityService;
        this.bpEntityRepository = bpEntityRepository;
        this.bpaEntityRepository = bpaEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteAll(jdbcTemplate);
    }

    @DisplayName("블로그 포스트 엔터티 전파 테스트")
    @Test
    void blogPostEntityPropagationTest() {
        // given
        bpaEntityRepository.save(createBlogPostArticleEntity());

        // when
        String name = blogPost.getName();
        Long number = bpEntityRepository.findByName(name).orElseThrow().getNumber();
        BlogPost updatedBlogPost = BlogPost.builder().blogPost(anotherBlogPost).number(number).build();
        entityManager.clear();
        bpEntityService.update(updatedBlogPost);

        // then
        entityManager.clear();
        assertThat(bpaEntityService.getByBlogPost(updatedBlogPost).size()).isEqualTo(1);
    }
}
