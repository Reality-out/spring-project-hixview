package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Article;
import springsideproject1.springsideproject1build.repository.ArticleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

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
    public void joinArticlesWithString(String subjectCompany, String articleString) {  // TODO: 링크가 비어 있습니다.
        List<List<String>> partialArticleLists = parseSingleString(articleString);

        for (List<String> partialArticleList : partialArticleLists) {
            joinArticle(new Article.ArticleBuilder()
                    .name(partialArticleList.get(0))
                    .press(partialArticleList.get(4))
                    .subjectCompany(subjectCompany)
                    .link("")
                    .date(LocalDate.of(parseInt(partialArticleList.get(1)),
                            parseInt(partialArticleList.get(2)),
                            parseInt(partialArticleList.get(3))))
                    .importance(0)
                    .build());
        }
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

    @Transactional
    private List<List<String>> parseSingleString(String articleString) {
        List<String> dividedArticle = List.of(articleString.split("\n"));
        List<List<String>> returnArticle = new ArrayList<>();
        List<String> tempArticle = new ArrayList<>();

        for (int i = 0; i < dividedArticle.size(); i++) {
            if (i % 2 == 0) {
                tempArticle.add(dividedArticle.get(i));
            } else {
                Collections.addAll(tempArticle, dividedArticle.get(i).replaceAll("^\\(|\\)$", "").split(", "));
                returnArticle.add(new ArrayList<>() {{
                        add(tempArticle.get(0));
                        addAll(List.of(tempArticle.get(1).split("-")));
                        add(tempArticle.get(2));
                    }}
                );
                tempArticle.clear();
            }
        }
        return returnArticle;
    }
}
