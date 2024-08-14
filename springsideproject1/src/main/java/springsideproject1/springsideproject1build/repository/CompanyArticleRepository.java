package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.article.CompanyArticle;

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

    Optional<CompanyArticle> getArticleByNumber(Long number);

    Optional<CompanyArticle> getArticleByName(String name);

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
