package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.Article;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    /**
     * SELECT Article
     */
    List<Article> findAllArticles();

    Optional<Article> searchArticleByName(String name);

    List<Article> searchArticlesByDate(LocalDate date);

    List<Article> searchArticlesByDate(LocalDate startDate, LocalDate endDate);

    /**
     * INSERT Company
     */
    Long saveOneArticle(Article article);

    /**
     * REMOVE Company
     */
    void removeArticleByName(String name);
}
