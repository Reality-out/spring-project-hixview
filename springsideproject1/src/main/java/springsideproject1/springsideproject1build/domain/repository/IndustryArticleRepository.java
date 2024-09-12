package springsideproject1.springsideproject1build.domain.repository;

import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IndustryArticleRepository extends ArticleRepository<IndustryArticle> {
    /**
     * SELECT IndustryArticle
     */
    List<IndustryArticle> getArticles();

    List<IndustryArticle> getArticlesByDate(LocalDate date);

    List<IndustryArticle> getArticlesByDate(LocalDate startDate, LocalDate endDate);

    List<IndustryArticle> getLatestArticles();

    Optional<IndustryArticle> getArticleByNumber(Long number);

    Optional<IndustryArticle> getArticleByName(String name);

    Optional<IndustryArticle> getArticleByLink(String link);

    /**
     * INSERT IndustryArticle
     */
    Long saveArticle(IndustryArticle article);

    /**
     * UPDATE IndustryArticle
     */
    void updateArticle(IndustryArticle article);

    /**
     * REMOVE IndustryArticle
     */
    void deleteArticleByName(String name);
}
