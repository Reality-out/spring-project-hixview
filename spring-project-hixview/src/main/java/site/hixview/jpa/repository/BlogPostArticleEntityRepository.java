package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.method.BasicMapperEntityRepositoryFunction;

import java.util.List;

@Repository
public interface BlogPostArticleEntityRepository extends BasicMapperEntityRepositoryFunction<BlogPostArticleEntity, BlogPostEntity, ArticleEntity>, JpaRepository<BlogPostArticleEntity, Long> {
    /**
     * SELECT BlogPostArticleMapper
     */
    List<BlogPostArticleEntity> findByBlogPost(BlogPostEntity post);

    List<BlogPostArticleEntity> findByArticle(ArticleEntity article);
}
