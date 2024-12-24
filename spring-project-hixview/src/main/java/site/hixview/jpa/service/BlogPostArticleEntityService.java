package site.hixview.jpa.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.error.EntityExistsWithNumberException;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.aggregate.service.BlogPostArticleService;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.mapper.BlogPostArticleEntityMapper;
import site.hixview.jpa.mapper.BlogPostArticleEntityMapperImpl;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;

import java.util.List;
import java.util.Optional;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.ARTICLE_NUMBER;
import static site.hixview.aggregate.vo.WordCamel.POST_NUMBER;
import static site.hixview.jpa.utils.MapperUtils.map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogPostArticleEntityService implements BlogPostArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final BlogPostEntityRepository blogPostEntityRepository;
    private final BlogPostArticleEntityRepository bpaEntityRepository;

    private final BlogPostArticleEntityMapper blogPostArticleEntityMapper = new BlogPostArticleEntityMapperImpl();

    @Override
    public List<BlogPostArticle> getAll() {
        return bpaEntityRepository.findAll().stream().map(blogPostArticleEntityMapper::toBlogPostArticle).toList();
    }

    @Override
    public Optional<BlogPostArticle> getByNumber(Long number) {
        return getOptionalBlogPostArticle(bpaEntityRepository.findByNumber(number).orElse(null));
    }

    @Override
    public List<BlogPostArticle> getByBlogPost(BlogPost blogPost) {
        return bpaEntityRepository.findByBlogPost(
                        blogPostEntityRepository.findByNumber(blogPost.getNumber()).orElseThrow())
                .stream().map(blogPostArticleEntityMapper::toBlogPostArticle).toList();
    }

    @Override
    public List<BlogPostArticle> getByArticle(Article article) {
        return bpaEntityRepository.findByArticle(
                        articleEntityRepository.findByNumber(article.getNumber()).orElseThrow())
                .stream().map(blogPostArticleEntityMapper::toBlogPostArticle).toList();
    }

    @Override
    @Transactional
    public BlogPostArticle insert(BlogPostArticle blogPostArticle) {
        Long number = blogPostArticle.getNumber();
        Long postNumber = blogPostArticle.getPostNumber();
        Long articleNumber = blogPostArticle.getArticleNumber();
        if (bpaEntityRepository.existsByNumber(number)) {
            throw new EntityExistsWithNumberException(number, BlogPostArticleEntity.class);
        }
        validateDuplicateEntity(postNumber, articleNumber);
        return blogPostArticleEntityMapper.toBlogPostArticle(bpaEntityRepository.save(
                blogPostArticleEntityMapper.toBlogPostArticleEntity(
                        blogPostArticle, blogPostEntityRepository, articleEntityRepository)));
    }

    @Override
    @Transactional
    public BlogPostArticle update(BlogPostArticle blogPostArticle) {
        Long number = blogPostArticle.getNumber();
        if (!bpaEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, BlogPostArticleEntity.class);
        }
        Long postNumber = blogPostArticle.getPostNumber();
        Long articleNumber = blogPostArticle.getArticleNumber();
        validateDuplicateEntity(postNumber, articleNumber);
        return blogPostArticleEntityMapper.toBlogPostArticle(bpaEntityRepository.save(
                map(blogPostArticle, bpaEntityRepository.findByNumber(number).orElseThrow(),
                        blogPostEntityRepository, articleEntityRepository)));
    }

    @Override
    @Transactional
    public void removeByNumber(Long number) {
        if (!bpaEntityRepository.existsByNumber(number)) {
            throw new EntityNotFoundWithNumberException(number, BlogPostArticleEntity.class);
        }
        bpaEntityRepository.deleteByNumber(number);
    }

    private Optional<BlogPostArticle> getOptionalBlogPostArticle(BlogPostArticleEntity blogPostArticleEntity) {
        if (blogPostArticleEntity == null) {
            return Optional.empty();
        }
        return Optional.of(blogPostArticleEntityMapper.toBlogPostArticle(blogPostArticleEntity));
    }

    private void validateDuplicateEntity(Long postNumber, Long articleNumber) {
        if (bpaEntityRepository.findByBlogPostAndArticle(
                blogPostEntityRepository.findByNumber(postNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(postNumber, BlogPostEntity.class)),
                articleEntityRepository.findByNumber(articleNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(articleNumber, ArticleEntity.class))
        ).isPresent()) {
            throw new EntityExistsException(getFormattedExceptionMessage(
                    ALREADY_EXISTED_ENTITY, POST_NUMBER, postNumber, BlogPostEntity.class,
                    ARTICLE_NUMBER, articleNumber, ArticleEntity.class));
        }
    }
}
