package springsideproject1.springsideproject1build.domain.repository;

import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleMain;

import java.util.List;
import java.util.Optional;

public interface CompanyArticleMainRepository {
    /**
     * SELECT CompanyArticleMain
     */
    List<CompanyArticleMain> getArticles();

    Optional<CompanyArticleMain> getArticleByNumber(Long number);

    Optional<CompanyArticleMain> getArticleByName(String name);

    Optional<CompanyArticleMain> getArticleByImagePath(String imagePath);

    /**
     * INSERT CompanyArticleMain
     */
    Long saveArticle(CompanyArticleMain article);

    /**
     * UPDATE CompanyArticleMain
     */
    void updateArticle(CompanyArticleMain article);

    /**
     * REMOVE CompanyArticleMain
     */
    void deleteArticleByName(String name);
}
