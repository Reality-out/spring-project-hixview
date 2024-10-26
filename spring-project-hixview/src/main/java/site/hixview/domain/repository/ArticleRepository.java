package site.hixview.domain.repository;

import site.hixview.domain.entity.article.parent.Article;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository<T extends Article<T>> {
    /**
     * SELECT Article
     */
    List<T> getArticles();

    List<T> getArticlesByDate(LocalDate date);

    List<T> getArticlesByDate(LocalDate startDate, LocalDate endDate);

    List<T> getLatestArticles();

    Optional<T> getArticleByNumber(Long number);

    Optional<T> getArticleByName(String name);

    Optional<T> getArticleByLink(String link);

    /**
     * INSERT Article
     */
    Long saveArticle(T article);

    /**
     * UPDATE Article
     */
    void updateArticle(T article);

    /**
     * REMOVE Article
     */
    void deleteArticleByName(String name);
}
