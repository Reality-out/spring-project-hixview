package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.method.BasicMapperEntityRepositoryFunction;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostArticleEntityRepository extends BasicMapperEntityRepositoryFunction<BlogPostArticleEntity, BlogPostEntity, ArticleEntity>, JpaRepository<BlogPostArticleEntity, Long> {
    /**
     * SELECT BlogPostArticleMapper
     */
    List<BlogPostArticleEntity> findByBlogPost(BlogPostEntity blogPost);

    List<BlogPostArticleEntity> findByArticle(ArticleEntity article);

    Optional<BlogPostArticleEntity> findByBlogPostAndArticle(BlogPostEntity blogPost, ArticleEntity article);
}
