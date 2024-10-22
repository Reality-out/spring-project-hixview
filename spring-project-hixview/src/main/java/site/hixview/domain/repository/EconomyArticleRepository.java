package site.hixview.domain.repository;

import site.hixview.domain.entity.article.EconomyArticle;

import java.util.List;

public interface EconomyArticleRepository extends ArticleRepository<EconomyArticle> {
    /**
     * SELECT EconomyArticle
     */
    List<EconomyArticle> getLatestDomesticArticles();

    List<EconomyArticle> getLatestForeignArticles();
}
