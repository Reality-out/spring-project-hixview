package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.BlogPostArticleMapperEntity;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface BlogPostArticleMapperRepository extends BasicMapperRepositoryFunction<BlogPostArticleMapperEntity, BlogPostEntity, ArticleEntity>, JpaRepository<BlogPostArticleMapperEntity, Long> {
    /**
     * SELECT BlogPostArticleMapper
     */
    List<BlogPostArticleMapperEntity> findByBlogPost(BlogPostEntity post);

    List<BlogPostArticleMapperEntity> findByArticle(ArticleEntity article);
}
