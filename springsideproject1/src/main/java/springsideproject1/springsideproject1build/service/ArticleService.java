package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Article;
import springsideproject1.springsideproject1build.repository.ArticleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * SELECT Articles
     */
    public List<Article> findArticles() {
        return articleRepository.findAllArticles();
    }

    public List<Article> SearchOneArticleByDate(LocalDate articleDate) {
        return articleRepository.searchArticlesByDate(articleDate);
    }

    public List<Article> SearchOneArticleByDate(LocalDate articleStartDate, LocalDate articleEndDate) {
        return articleRepository.searchArticlesByDate(articleStartDate, articleEndDate);
    }

    /**
     * SELECT One Article
     */
    public Optional<Article> SearchOneArticleByName(String articleName) {
        return articleRepository.searchArticleByName(articleName);
    }

    /**
     * INSERT One Article
     */
    @Transactional
    public void joinArticle(Article article) {
        duplicateCodeCheck(article);
        Long articleNumber = articleRepository.saveOneArticle(article);
        article = new Article.ArticleBuilder()
                .article(article)
                .number(articleNumber)
                .build();
    }

    @Transactional
    public void joinArticlesWithString(String string) {

    }

    /**
     * REMOVE One Article
     */
    @Transactional
    public void removeArticle(String articleName) {
        articleRepository.searchArticleByName(articleName).orElseThrow(
                () -> new IllegalStateException("해당 제목과 일치하는 기사가 없습니다.")
        );

        articleRepository.removeArticleByName(articleName);
    }

    /**
     * Other private methods
     */
    @Transactional
    private void duplicateCodeCheck(Article article) {
        articleRepository.searchArticleByName(article.getName()).ifPresent(
                v -> {throw new IllegalStateException("이미 존재하는 기사 제목입니다.");}
        );
    }
}
