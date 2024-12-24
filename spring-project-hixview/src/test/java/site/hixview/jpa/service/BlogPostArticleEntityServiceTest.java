package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.mapper.*;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.BlogPostArticleEntityTestUtils;
import site.hixview.support.jpa.util.BlogPostEntityTestUtils;
import site.hixview.support.spring.util.BlogPostArticleTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.*;

@OnlyRealServiceContext
@Slf4j
class BlogPostArticleEntityServiceTest implements BlogPostEntityTestUtils, BlogPostArticleEntityTestUtils, BlogPostArticleTestUtils {

    private final BlogPostArticleEntityService bpaEntityService;
    private final ArticleEntityRepository articleEntityRepository;
    private final BlogPostEntityRepository bpEntityRepository;
    private final BlogPostArticleEntityRepository bpaEntityRepository;

    private final BlogPostArticleEntityMapper bpaEntityMapper = new BlogPostArticleEntityMapperImpl();
    private final BlogPostEntityMapper bpEntityMapper = new BlogPostEntityMapperImpl();
    private final ArticleEntityMapper articleEntityMapper = new ArticleEntityMapperImpl();

    @Autowired
    BlogPostArticleEntityServiceTest(BlogPostArticleEntityService bpaEntityService, ArticleEntityRepository articleEntityRepository, BlogPostEntityRepository bpEntityRepository, BlogPostArticleEntityRepository bpaEntityRepository) {
        this.bpaEntityService = bpaEntityService;
        this.articleEntityRepository = articleEntityRepository;
        this.bpEntityRepository = bpEntityRepository;
        this.bpaEntityRepository = bpaEntityRepository;
    }

    @DisplayName("모든 블로그 포스트와 기사 간 매퍼 획득")
    @Test
    void getAllTest() {
        // given
        BlogPostArticleEntity bpaEntity = createBlogPostArticleEntity();
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.findAll()).thenReturn(List.of(bpaEntity));

        // when
        bpaEntityRepository.save(bpaEntity);

        // then
        assertThat(bpaEntityService.getAll()).isEqualTo(List.of(bpaEntityMapper.toBlogPostArticle(bpaEntity)));
    }


    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 획득")
    @Test
    void getByNumberTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        Long number = bpaEntity.getNumber();
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.findByNumber(number)).thenReturn(Optional.of(bpaEntity));

        // when
        bpaEntityRepository.save(bpaEntity);

        // then
        assertThat(bpaEntityService.getByNumber(number).orElseThrow()).isEqualTo(bpaEntityMapper.toBlogPostArticle(bpaEntity));
    }

    @DisplayName("블로그 포스트로 블로그 포스트와 기사 간 매퍼 획득")
    @Test
    void getByBlogPostTest() {
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        BlogPostEntity bpEntity = bpaEntity.getBlogPost();
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.findByBlogPost(bpEntity)).thenReturn(List.of(bpaEntity));
        when(bpEntityRepository.findByNumber(bpEntity.getPost().getNumber())).thenReturn(Optional.of(bpEntity));

        // when
        bpaEntityRepository.save(bpaEntity);

        // then
        assertThat(bpaEntityService.getByBlogPost(bpEntityMapper.toBlogPost(bpEntity, bpaEntityRepository))).isEqualTo(List.of(bpaEntityMapper.toBlogPostArticle(bpaEntity)));
    }

    @DisplayName("기사로 블로그 포스트와 기사 간 매퍼 획득")
    @Test
    void getByArticleTest() {
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        ArticleEntity articleEntity = bpaEntity.getArticle();
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.findByArticle(articleEntity)).thenReturn(List.of(bpaEntity));
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));

        // when
        bpaEntityRepository.save(bpaEntity);

        // then
        assertThat(bpaEntityService.getByArticle(articleEntityMapper.toArticle(articleEntity))).isEqualTo(List.of(bpaEntityMapper.toBlogPostArticle(bpaEntity)));
    }

    @DisplayName("블로그 포스트와 기사 간 매퍼 삽입")
    @Test
    void insertTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        BlogPostEntity bpEntity = bpaEntity.getBlogPost();
        ArticleEntity articleEntity = bpaEntity.getArticle();
        BlogPostArticle blogPostArticle = bpaEntityMapper.toBlogPostArticle(bpaEntity);
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(bpaEntityRepository.existsByNumber(blogPostArticle.getNumber())).thenReturn(false);
        when(bpaEntityRepository.findByBlogPostAndArticle(bpaEntity.getBlogPost(), articleEntity)).thenReturn(Optional.empty());
        when(bpEntityRepository.findByNumber(blogPostArticle.getPostNumber())).thenReturn(Optional.of(bpEntity));
        when(articleEntityRepository.findByNumber(blogPostArticle.getArticleNumber())).thenReturn(Optional.of(articleEntity));
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.findAll()).thenReturn(List.of(bpaEntity));
        bpEntityRepository.save(bpEntity);
        articleEntityRepository.save(articleEntity);

        // when
        bpaEntityService.insert(blogPostArticle);

        // then
        assertThat(bpaEntityService.getAll()).isEqualTo(List.of(blogPostArticle));
    }

    @DisplayName("이미 존재하는 번호로 블로그 포스트와 기사 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        BlogPostEntity bpEntity = bpaEntity.getBlogPost();
        ArticleEntity articleEntity = bpaEntity.getArticle();
        BlogPostArticle blogPostArticle = bpaEntityMapper.toBlogPostArticle(bpaEntity);
        Long number = blogPostArticle.getNumber();
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.existsByNumber(number)).thenReturn(true);

        // when
        bpEntityRepository.save(bpEntity);
        articleEntityRepository.save(articleEntity);
        bpaEntityRepository.save(bpaEntity);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> bpaEntityService.insert(BlogPostArticle.builder()
                        .blogPostArticle(anotherBlogPostArticle).number(number).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, number, BlogPostArticleEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 블로그 포스트와 기사 간 매퍼 삽입")
    @Test
    void insertAlreadyExistedNumberPairTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        BlogPostEntity bpEntity = bpaEntity.getBlogPost();
        ArticleEntity articleEntity = bpaEntity.getArticle();
        BlogPostArticle blogPostArticle = BlogPostArticle.builder().blogPostArticle(bpaEntityMapper.toBlogPostArticle(bpaEntity)).postNumber(bpEntity.getPost().getNumber()).build();
        BlogPostArticleEntity bpaEntityExistedNumberPair = new BlogPostArticleEntity(anotherBlogPostArticle.getNumber(), bpEntity, articleEntity);
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.existsByNumber(blogPostArticle.getNumber())).thenReturn(false);
        when(bpaEntityRepository.findByBlogPostAndArticle(bpEntity, articleEntity)).thenReturn(Optional.of(bpaEntityExistedNumberPair));
        when(bpEntityRepository.findByNumber(blogPostArticle.getPostNumber())).thenReturn(Optional.of(bpEntity));
        when(articleEntityRepository.findByNumber(blogPostArticle.getArticleNumber())).thenReturn(Optional.of(articleEntity));

        // when
        bpEntityRepository.save(bpEntity);
        articleEntityRepository.save(articleEntity);
        bpaEntityRepository.save(bpaEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> bpaEntityService.insert(BlogPostArticle.builder().
                        blogPostArticle(bpaEntityMapper.toBlogPostArticle(bpaEntityExistedNumberPair)).
                        postNumber(bpEntity.getPost().getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, POST_NUMBER, bpEntity.getPost().getNumber(), BlogPostEntity.class,
                ARTICLE_NUMBER, articleEntity.getNumber(), ArticleEntity.class));
    }

    @DisplayName("블로그 포스트와 기사 간 매퍼 갱신")
    @Test
    void updateTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        BlogPostEntity bpEntity = bpaEntity.getBlogPost();
        ArticleEntity articleEntity = bpaEntity.getArticle();
        BlogPostEntity bpEntityUpdated = createAnotherBlogPostEntity();
        PostEntity postEntityUpdated = createAnotherPostEntity();
        postEntityUpdated.updateNumber(anotherPost.getNumber());
        bpEntityUpdated.updatePost(postEntityUpdated);
        ArticleEntity articleEntityUpdated = createAnotherArticleEntity();
        articleEntityUpdated.updateNumber(anotherArticle.getNumber());

        BlogPostArticle bpaUpdated = BlogPostArticle.builder().number(bpaEntity.getNumber()).postNumber(postEntityUpdated.getNumber()).articleNumber(articleEntityUpdated.getNumber()).build();
        when(bpEntityRepository.findByNumber(bpEntityUpdated.getPost().getNumber())).thenReturn(Optional.of(bpEntityUpdated));
        when(articleEntityRepository.findByNumber(articleEntityUpdated.getNumber())).thenReturn(Optional.of(articleEntityUpdated));
        BlogPostArticleEntity bpaEntityUpdated = bpaEntityMapper.toBlogPostArticleEntity(bpaUpdated, bpEntityRepository, articleEntityRepository);
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity).thenReturn(bpaEntityUpdated);
        when(bpEntityRepository.save(bpEntityUpdated)).thenReturn(bpEntityUpdated);
        when(articleEntityRepository.save(articleEntityUpdated)).thenReturn(articleEntityUpdated);
        when(bpaEntityRepository.existsByNumber(bpaUpdated.getNumber())).thenReturn(true);
        when(bpaEntityRepository.findByBlogPostAndArticle(bpEntityUpdated, articleEntityUpdated)).thenReturn(Optional.empty());
        when(bpaEntityRepository.findByNumber(bpaEntityUpdated.getNumber())).thenReturn(Optional.of(bpaEntityUpdated));
        when(bpEntityRepository.findByNumber(bpaUpdated.getPostNumber())).thenReturn(Optional.of(bpEntityUpdated));
        when(articleEntityRepository.findByNumber(bpaUpdated.getArticleNumber())).thenReturn(Optional.of(articleEntityUpdated));
        when(bpaEntityRepository.findAll()).thenReturn(List.of(bpaEntityUpdated));

        bpEntityRepository.save(bpEntity);
        articleEntityRepository.save(articleEntity);
        bpaEntityRepository.save(bpaEntity);
        bpEntityRepository.save(bpEntityUpdated);
        articleEntityRepository.save(articleEntityUpdated);

        // when
        bpaEntityService.update(bpaUpdated);

        // then
        assertThat(bpaEntityService.getAll()).isEqualTo(List.of(BlogPostArticle.builder().blogPostArticle(bpaUpdated).postNumber(null).build()));
    }

    @DisplayName("발견되지 않는 번호로 블로그 포스트와 기사 간 매퍼 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        Long number = bpaEntity.getNumber();

        // when
        when(bpaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                bpaEntityService.update(bpaEntityMapper.toBlogPostArticle(bpaEntity)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, BlogPostArticleEntity.class));
    }

    @DisplayName("이미 존재하는 번호 쌍으로 블로그 포스트와 기사 간 매퍼 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        BlogPostEntity bpEntity = bpaEntity.getBlogPost();
        ArticleEntity articleEntity = bpaEntity.getArticle();

        BlogPostArticle bpaUpdated = BlogPostArticle.builder().number(bpaEntity.getNumber()).postNumber(bpEntity.getPost().getNumber()).articleNumber(articleEntity.getNumber()).build();
        when(bpEntityRepository.findByNumber(bpEntity.getPost().getNumber())).thenReturn(Optional.of(bpEntity));
        when(articleEntityRepository.findByNumber(articleEntity.getNumber())).thenReturn(Optional.of(articleEntity));
        BlogPostArticleEntity bpaEntityUpdated = bpaEntityMapper.toBlogPostArticleEntity(bpaUpdated, bpEntityRepository, articleEntityRepository);
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(articleEntityRepository.save(articleEntity)).thenReturn(articleEntity);
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity).thenReturn(bpaEntityUpdated);
        when(bpaEntityRepository.existsByNumber(bpaUpdated.getNumber())).thenReturn(true);
        when(bpaEntityRepository.findByBlogPostAndArticle(bpEntity, articleEntity)).thenReturn(Optional.of(bpaEntity));
        when(bpaEntityRepository.findByNumber(bpaEntityUpdated.getNumber())).thenReturn(Optional.of(bpaEntityUpdated));
        when(bpEntityRepository.findByNumber(bpaUpdated.getPostNumber())).thenReturn(Optional.of(bpEntity));
        when(articleEntityRepository.findByNumber(bpaUpdated.getArticleNumber())).thenReturn(Optional.of(articleEntity));

        // when
        bpEntityRepository.save(bpEntity);
        articleEntityRepository.save(articleEntity);
        bpaEntityRepository.save(bpaEntity);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                bpaEntityService.update(bpaEntityService.update(bpaUpdated)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, POST_NUMBER, bpEntity.getPost().getNumber(), BlogPostEntity.class,
                ARTICLE_NUMBER, articleEntity.getNumber(), ArticleEntity.class));
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼 제거")
    @Test
    void removeByNumberTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        when(bpaEntityRepository.save(bpaEntity)).thenReturn(bpaEntity);
        when(bpaEntityRepository.existsByNumber(bpaEntity.getNumber())).thenReturn(true);
        doNothing().when(bpaEntityRepository).deleteByNumber(bpaEntity.getNumber());
        when(bpaEntityRepository.findAll()).thenReturn(Collections.emptyList());
        bpaEntityRepository.save(bpaEntity);

        // when
        bpaEntityService.removeByNumber(bpaEntity.getNumber());

        // then
        assertThat(bpaEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 블로그 포스트와 기사 간 매퍼 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        BlogPostArticleEntity bpaEntity = createNumberedBlogPostArticleEntity();
        Long number = bpaEntity.getNumber();

        // when
        when(bpaEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> bpaEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, BlogPostArticleEntity.class));
    }
}