package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.article.CompanyArticle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompanyArticleRepository {
    /**
     * SELECT CompanyArticle
     */
    List<CompanyArticle> selectArticles();

    List<CompanyArticle> selectArticlesByDate(LocalDate date);

    List<CompanyArticle> selectArticlesByDate(LocalDate startDate, LocalDate endDate);

    Optional<CompanyArticle> selectArticleByNumber(Long number);

    Optional<CompanyArticle> selectArticleByName(String name);

    /**
     * INSERT CompanyArticle
     */
    Long insertArticle(CompanyArticle article);

    /**
     * UPDATE CompanyArticle
     */
    void updateArticle(CompanyArticle article);

    /**
     * REMOVE CompanyArticle
     */
    void deleteArticleByName(String name);
}
