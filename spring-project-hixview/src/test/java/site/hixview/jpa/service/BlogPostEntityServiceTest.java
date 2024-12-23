package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.mapper.BlogPostEntityMapper;
import site.hixview.jpa.mapper.BlogPostEntityMapperImpl;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;
import site.hixview.support.jpa.context.OnlyRealServiceContext;
import site.hixview.support.jpa.util.BlogPostArticleEntityTestUtils;
import site.hixview.support.jpa.util.BlogPostEntityTestUtils;
import site.hixview.support.spring.util.BlogPostTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.*;
import static site.hixview.aggregate.vo.WordCamel.NAME;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@OnlyRealServiceContext
@Slf4j
class BlogPostEntityServiceTest implements BlogPostEntityTestUtils, BlogPostArticleEntityTestUtils, BlogPostTestUtils {

    private final BlogPostEntityService bpEntityService;
    private final BlogPostEntityRepository bpEntityRepository;
    private final BlogPostArticleEntityRepository bpaEntityRepository;
    private final PostEntityRepository postEntityRepository;

    private final BlogPostEntityMapper bpEntityMapper = new BlogPostEntityMapperImpl();

    @Autowired
    BlogPostEntityServiceTest(BlogPostEntityService bpEntityService, BlogPostEntityRepository bpEntityRepository, BlogPostArticleEntityRepository bpaEntityRepository, PostEntityRepository postEntityRepository) {
        this.bpEntityService = bpEntityService;
        this.bpEntityRepository = bpEntityRepository;
        this.bpaEntityRepository = bpaEntityRepository;
        this.postEntityRepository = postEntityRepository;
    }

    @DisplayName("모든 블로그 포스트 획득")
    @Test
    void getAllTest() {
        // given
        BlogPostEntity blogPostEntity = createBlogPostEntity();
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findAll()).thenReturn(List.of(blogPostEntity));

        // when
        bpEntityRepository.save(blogPostEntity);

        // then
        assertThat(bpEntityService.getAll()).isEqualTo(List.of(bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository)));
    }

    @DisplayName("날짜로 블로그 포스트 획득")
    @Test
    void getByDateTest() {
        BlogPostEntity blogPostEntity = createBlogPostEntity();
        LocalDate date = blogPostEntity.getDate();
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findByDate(date)).thenReturn(List.of(blogPostEntity));

        // when
        bpEntityRepository.save(blogPostEntity);

        // then
        assertThat(bpEntityService.getByDate(date)).isEqualTo(List.of(bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository)));
    }

    @DisplayName("날짜 범위로 블로그 포스트 획득")
    @Test
    void getByDateRangeTest() {
        BlogPostEntity blogPostEntity = createBlogPostEntity();
        LocalDate date = blogPostEntity.getDate();
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findByDateBetween(date, date)).thenReturn(List.of(blogPostEntity));

        // when
        bpEntityRepository.save(blogPostEntity);

        // then
        assertThat(bpEntityService.getByDateRange(date, date)).isEqualTo(List.of(bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository)));
    }

    @DisplayName("분류로 블로그 포스트 획득")
    @Test
    void getByClassificationTest() {
        BlogPostEntity blogPostEntity = createBlogPostEntity();
        String classification = blogPostEntity.getClassification();
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findByClassification(classification)).thenReturn(List.of(blogPostEntity));

        // when
        bpEntityRepository.save(blogPostEntity);

        // then
        assertThat(bpEntityService.getByClassification(Classification.valueOf(classification))).isEqualTo(List.of(bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository)));
    }

    @DisplayName("번호로 블로그 포스트 획득")
    @Test
    void getByNumberTest() {
        // given
        BlogPostEntity blogPostEntity = createNumberedBlogPostEntity();
        Long number = blogPostEntity.getNumber();
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findByNumber(number)).thenReturn(Optional.of(blogPostEntity));

        // when
        bpEntityRepository.save(blogPostEntity);

        // then
        assertThat(bpEntityService.getByNumber(number).orElseThrow()).isEqualTo(bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository));
    }

    @DisplayName("이름으로 블로그 포스트 획득")
    @Test
    void getByNameTest() {
        // given
        BlogPostEntity blogPostEntity = createBlogPostEntity();
        String name = blogPostEntity.getName();
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findByName(name)).thenReturn(Optional.of(blogPostEntity));

        // when
        bpEntityRepository.save(blogPostEntity);

        // then
        assertThat(bpEntityService.getByName(name).orElseThrow()).isEqualTo(bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository));
    }

    @DisplayName("링크로 블로그 포스트 획득")
    @Test
    void getByLinkTest() {
        // given
        BlogPostEntity blogPostEntity = createBlogPostEntity();
        String link = blogPostEntity.getLink();
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findByLink(link)).thenReturn(Optional.of(blogPostEntity));

        // when
        bpEntityRepository.save(blogPostEntity);

        // then
        assertThat(bpEntityService.getByLink(link).orElseThrow()).isEqualTo(bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository));
    }

    @DisplayName("블로그 포스트 삽입")
    @Test
    void insertTest() {
        // given
        BlogPostEntity blogPostEntity = createNumberedBlogPostEntity();
        PostEntity postEntity = blogPostEntity.getPost();
        BlogPost blogPost = bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository);
        when(postEntityRepository.save(postEntity)).thenReturn(postEntity);
        when(bpEntityRepository.existsByNumber(blogPost.getNumber())).thenReturn(false);
        when(bpEntityRepository.findByName(blogPost.getName())).thenReturn(Optional.empty());
        when(postEntityRepository.findByNumber(postEntity.getNumber())).thenReturn(Optional.of(postEntity));
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        when(bpEntityRepository.findAll()).thenReturn(List.of(blogPostEntity));
        postEntityRepository.save(postEntity);

        // when
        bpEntityService.insert(blogPost);

        // then
        assertThat(bpEntityService.getAll()).isEqualTo(List.of(blogPost));
    }

    @DisplayName("이미 존재하는 번호로 블로그 포스트 삽입")
    @Test
    void insertAlreadyExistedNumberTest() {
        // given
        BlogPostEntity blogPostEntity = createNumberedBlogPostEntity();
        PostEntity postEntity = blogPostEntity.getPost();
        BlogPost blogPost = bpEntityMapper.toBlogPost(blogPostEntity, bpaEntityRepository);
        when(postEntityRepository.save(postEntity)).thenReturn(postEntity);
        when(bpEntityRepository.existsByNumber(blogPost.getNumber())).thenReturn(false).thenReturn(true);
        when(bpEntityRepository.findByName(blogPost.getName())).thenReturn(Optional.empty());
        when(postEntityRepository.findByNumber(postEntity.getNumber())).thenReturn(Optional.of(postEntity));
        when(bpEntityRepository.save(blogPostEntity)).thenReturn(blogPostEntity);
        postEntityRepository.save(postEntity);

        // when
        bpEntityService.insert(blogPost);

        // then
        EntityExistsWithNumberException exception = assertThrows(EntityExistsWithNumberException.class,
                () -> bpEntityService.insert(BlogPost.builder()
                        .blogPost(anotherBlogPost).number(blogPost.getNumber()).build()));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NUMBER, blogPost.getNumber(), BlogPostEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 블로그 포스트 삽입")
    @Test
    void insertAlreadyExistedNameTest() {
        // given
        BlogPostEntity bpEntity = createNumberedBlogPostEntity();
        BlogPostEntity bpEntityExistedEnglishName = BlogPostEntity.builder()
                .blogPost(createAnotherBlogPostEntity()).post(bpEntity.getPost()).name(bpEntity.getName()).build();
        PostEntity postEntity = bpEntity.getPost();
        Long number = postEntity.getNumber();
        BlogPost blogPost = bpEntityMapper.toBlogPost(bpEntity, bpaEntityRepository);
        when(postEntityRepository.save(postEntity)).thenReturn(postEntity);
        when(bpEntityRepository.existsByNumber(number)).thenReturn(false);
        when(bpEntityRepository.findByName(blogPost.getName())).thenReturn(Optional.empty()).thenReturn(Optional.of(bpEntity));
        when(postEntityRepository.findByNumber(number)).thenReturn(Optional.of(postEntity));
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        postEntityRepository.save(postEntity);

        // when
        bpEntityService.insert(blogPost);

        // then
        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> bpEntityService.insert(bpEntityMapper.toBlogPost(bpEntityExistedEnglishName, bpaEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, bpEntity.getName(), BlogPostEntity.class));
    }

    @DisplayName("블로그 포스트 갱신")
    @Test
    void updateTest() {
        // given
        BlogPostEntity bpEntity = createNumberedBlogPostEntity();
        PostEntity postEntity = bpEntity.getPost();
        Long number = postEntity.getNumber();
        BlogPost bpUpdated = BlogPost.builder().blogPost(anotherBlogPost).number(number).mappedArticleNumbers(Collections.emptyList()).build();
        when(postEntityRepository.findByNumber(number)).thenReturn(Optional.of(postEntity));
        BlogPostEntity bpEntityUpdated = bpEntityMapper.toBlogPostEntity(bpUpdated, postEntityRepository);
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(bpEntityRepository.save(bpEntityUpdated)).thenReturn(bpEntityUpdated);
        when(bpEntityRepository.existsByNumber(number)).thenReturn(true);
        when(bpEntityRepository.findByName(bpEntityUpdated.getName())).thenReturn(Optional.empty());
        when(postEntityRepository.findByNumber(postEntity.getNumber())).thenReturn(Optional.of(postEntity));
        when(bpaEntityRepository.findByBlogPost(bpEntityUpdated)).thenReturn(Collections.emptyList());
        when(bpEntityRepository.findAll()).thenReturn(List.of(bpEntityUpdated));
        bpEntityRepository.save(bpEntity);

        // when
        bpEntityService.update(bpUpdated);

        // then
        assertThat(bpEntityService.getAll()).isEqualTo(List.of(bpUpdated));
    }

    @DisplayName("발견되지 않는 번호로 블로그 포스트 갱신")
    @Test
    void updateNotFoundNumberTest() {
        // given
        BlogPostEntity bpEntity = createNumberedBlogPostEntity();
        Long number = bpEntity.getPost().getNumber();

        // when
        when(bpEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class, () ->
                bpEntityService.update(bpEntityMapper.toBlogPost(bpEntity, bpaEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, BlogPostEntity.class));
    }

    @DisplayName("이미 존재하는 이름으로 블로그 포스트 갱신")
    @Test
    void updateAlreadyExistedNameTest() {
        // given
        BlogPostEntity bpEntity = createBlogPostEntity();
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(bpEntityRepository.existsByNumber(any())).thenReturn(true);
        when(bpEntityRepository.findByName(bpEntity.getName())).thenReturn(Optional.of(bpEntity));

        // when
        bpEntityRepository.save(bpEntity);

        // then
        EntityExistsWithNameException exception = assertThrows(EntityExistsWithNameException.class, () ->
                bpEntityService.update(bpEntityMapper.toBlogPost(bpEntity, bpaEntityRepository)));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                ALREADY_EXISTED_ENTITY, NAME, bpEntity.getName(), BlogPostEntity.class));
    }

    @DisplayName("번호로 블로그 포스트 제거")
    @Test
    void removeByNumberTest() {
        // given
        BlogPostEntity bpEntity = createNumberedBlogPostEntity();
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(bpEntityRepository.existsByNumber(bpEntity.getNumber())).thenReturn(true);
        when(bpEntityRepository.findByNumber(bpEntity.getNumber())).thenReturn(Optional.of(bpEntity));
        when(bpaEntityRepository.findByBlogPost(bpEntity)).thenReturn(Collections.emptyList());
        doNothing().when(bpEntityRepository).deleteByNumber(bpEntity.getNumber());
        when(bpEntityRepository.findAll()).thenReturn(Collections.emptyList());
        bpEntityRepository.save(bpEntity);

        // when
        bpEntityService.removeByNumber(bpEntity.getNumber());

        // then
        assertThat(bpEntityService.getAll()).isEmpty();
    }

    @DisplayName("발견되지 않는 번호로 블로그 포스트 제거")
    @Test
    void removeByNotFoundNumberTest() {
        // given
        BlogPostEntity bpEntity = createNumberedBlogPostEntity();
        Long number = bpEntity.getNumber();

        // when
        when(bpEntityRepository.existsByNumber(number)).thenReturn(false);

        // then
        EntityNotFoundWithNumberException exception = assertThrows(EntityNotFoundWithNumberException.class,
                () -> bpEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                CANNOT_FOUND_ENTITY, NUMBER, number, BlogPostEntity.class));
    }

    @DisplayName("번호로 블로그 포스트와 기사 간 매퍼에 포함된 블로그 포스트 제거")
    @Test
    void removeByNumberInMapperTest() {
        // given
        BlogPostEntity bpEntity = createNumberedBlogPostEntity();
        Long number = bpEntity.getNumber();
        when(bpEntityRepository.save(bpEntity)).thenReturn(bpEntity);
        when(bpEntityRepository.existsByNumber(bpEntity.getNumber())).thenReturn(true);
        when(bpEntityRepository.findByNumber(bpEntity.getNumber())).thenReturn(Optional.of(bpEntity));
        when(bpaEntityRepository.findByBlogPost(bpEntity)).thenReturn(List.of(createBlogPostArticleEntity()));

        // when
        bpEntityRepository.save(bpEntity);

        // then
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class, () -> bpEntityService.removeByNumber(number));
        assertThat(exception.getMessage()).isEqualTo(getFormattedExceptionMessage(
                REMOVE_REFERENCED_ENTITY, NUMBER, number, BlogPostEntity.class));
    }
}