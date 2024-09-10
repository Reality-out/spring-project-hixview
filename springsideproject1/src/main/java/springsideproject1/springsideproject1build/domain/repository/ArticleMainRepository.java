package springsideproject1.springsideproject1build.domain.repository;


import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;

import java.util.List;
import java.util.Optional;

public interface ArticleMainRepository {
    /**
     * SELECT ArticleMain
     */
    List<ArticleMain> getArticles();

    Optional<ArticleMain> getArticleByNumber(Long number);

    Optional<ArticleMain> getArticleByName(String name);

    Optional<ArticleMain> getArticleByImagePath(String imagePath);

    /**
     * INSERT ArticleMain
     */
    Long saveArticle(ArticleMain article);

    /**
     * UPDATE ArticleMain
     */
    void updateArticle(ArticleMain article);

    /**
     * REMOVE ArticleMain
     */
    void deleteArticleByName(String name);
}
