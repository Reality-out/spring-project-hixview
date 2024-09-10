package springsideproject1.springsideproject1build.domain.repository;

import springsideproject1.springsideproject1build.domain.entity.article.company.CompanyArticle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompanyArticleRepository {
    /**
     * SELECT CompanyArticle
     */
    List<CompanyArticle> getArticles();

    List<CompanyArticle> getArticlesByDate(LocalDate date);

    List<CompanyArticle> getArticlesByDate(LocalDate startDate, LocalDate endDate);

    List<CompanyArticle> getLatestArticles();

    Optional<CompanyArticle> getArticleByNumber(Long number);

    Optional<CompanyArticle> getArticleByName(String name);

    Optional<CompanyArticle> getArticleByLink(String link);

    /**
     * INSERT CompanyArticle
     */
    Long saveArticle(CompanyArticle article);

    /**
     * UPDATE CompanyArticle
     */
    void updateArticle(CompanyArticle article);

    /**
     * REMOVE CompanyArticle
     */
    void deleteArticleByName(String name);
}
