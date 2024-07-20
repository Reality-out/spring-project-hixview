package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.CompanyArticle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompanyArticleRepository {
    /**
     * SELECT CompanyArticle
     */
    List<CompanyArticle> findAllArticles();

    Optional<CompanyArticle> searchArticleByName(String name);

    List<CompanyArticle> searchArticlesByDate(LocalDate date);

    List<CompanyArticle> searchArticlesByDate(LocalDate startDate, LocalDate endDate);

    /**
     * INSERT Company
     */
    Long saveOneArticle(CompanyArticle article);

    /**
     * REMOVE Company
     */
    void removeArticleByName(String name);
}
