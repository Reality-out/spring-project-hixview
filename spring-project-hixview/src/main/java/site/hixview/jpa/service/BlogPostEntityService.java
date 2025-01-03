package site.hixview.jpa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.error.EntityExistsWithNameException;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.BlogPostService;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.mapper.BlogPostEntityMapper;
import site.hixview.jpa.mapper.BlogPostEntityMapperImpl;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;
import site.hixview.jpa.utils.MapperUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.REMOVE_REFERENCED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BlogPostEntityService implements BlogPostService {

    private final PostEntityRepository postEntityRepository;
    private final BlogPostEntityRepository bpEntityRepository;
    private final BlogPostArticleEntityRepository bpaEntityRepository;

    private final BlogPostEntityMapper mapper = new BlogPostEntityMapperImpl();

    @Override
    public List<BlogPost> getAll() {
        return bpEntityRepository.findAll().stream().map((BlogPostEntity blogPostEntity) ->
                mapper.toBlogPost(blogPostEntity, bpaEntityRepository)).toList();
    }

    @Override
    public List<BlogPost> getByDate(LocalDate date) {
        return bpEntityRepository.findByDate(date).stream().map((BlogPostEntity blogPostEntity) ->
                mapper.toBlogPost(blogPostEntity, bpaEntityRepository)).toList();
    }

    @Override
    public List<BlogPost> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return bpEntityRepository.findByDateBetween(startDate, endDate).stream()
                .map((BlogPostEntity blogPostEntity) ->
                        mapper.toBlogPost(blogPostEntity, bpaEntityRepository)).toList();
    }

    @Override
    public List<BlogPost> getByClassification(Classification classification) {
        return bpEntityRepository.findByClassification(classification.name()).stream()
                .map((BlogPostEntity blogPostEntity) ->
                        mapper.toBlogPost(blogPostEntity, bpaEntityRepository)).toList();
    }

    @Override
    public Optional<BlogPost> getByNumber(Long number) {
        return getOptionalBlogPost(bpEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public Optional<BlogPost> getByName(String name) {
        return getOptionalBlogPost(bpEntityRepository.findByName(name).orElse(null));
    }

    @Override
    public Optional<BlogPost> getByLink(String link) {
        return getOptionalBlogPost(bpEntityRepository.findByLink(link).orElse(null));
    }

    @Override
    @Transactional
    public BlogPost insert(BlogPost blogPost) {
        Long number = blogPost.getNumber();
        String name = blogPost.getName();
        if (bpEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, BlogPostEntity.class);
        }
        if (bpEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, BlogPostEntity.class);
        }
        return mapper.toBlogPost(bpEntityRepository.save(mapper.toBlogPostEntity(
                blogPost, postEntityRepository)), bpaEntityRepository);
    }

    @Override
    @Transactional
    public BlogPost update(BlogPost blogPost) {
        Long number = blogPost.getNumber();
        String name = blogPost.getName();
        if (!bpEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, BlogPostEntity.class);
        }
        if (bpEntityRepository.findByName(name).isPresent()) {
            throw new EntityExistsWithNameException(name, BlogPostEntity.class);
        }
        BlogPostEntity blogPostEntity = bpEntityRepository.save(MapperUtils.map(
                blogPost, bpEntityRepository.findByNumber(blogPost.getNumber()).orElseThrow(), postEntityRepository));
        propagateBlogPostEntity(blogPostEntity);
        return mapper.toBlogPost(blogPostEntity, bpaEntityRepository);
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!bpEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, BlogPostEntity.class);
        }
        BlogPostEntity blogPostEntity = bpEntityRepository.findByNumber(number).orElseThrow();
        if (!bpaEntityRepository.findByBlogPost(blogPostEntity).isEmpty()) {
            throw new DataIntegrityViolationException(getFormattedExceptionMessage(
                    REMOVE_REFERENCED_ENTITY, NUMBER, number, BlogPostEntity.class));
        }
        bpEntityRepository.deleteByNumber(number);
    }

    private Optional<BlogPost> getOptionalBlogPost(BlogPostEntity optionalBlogPostEntity) {
        if (optionalBlogPostEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toBlogPost(optionalBlogPostEntity, bpaEntityRepository));
    }

    private void propagateBlogPostEntity(BlogPostEntity blogPostEntity) {
        bpaEntityRepository.saveAll(bpaEntityRepository.findByBlogPost(blogPostEntity).stream()
                .peek(mapper -> mapper.updateBlogPost(blogPostEntity)).toList());
    }
}
