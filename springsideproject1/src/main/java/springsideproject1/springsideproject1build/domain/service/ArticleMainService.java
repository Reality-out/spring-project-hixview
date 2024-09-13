package springsideproject1.springsideproject1build.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.error.AlreadyExistException;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;
import springsideproject1.springsideproject1build.domain.repository.ArticleMainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_MESSAGE.ALREADY_EXIST_ARTICLE_MAIN_NAME;
import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_MESSAGE.NO_ARTICLE_MAIN_WITH_THAT_NAME;
import static springsideproject1.springsideproject1build.domain.vo.REGEX.NUMBER_REGEX_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleMainService {

    private final ArticleMainRepository articleRepository;

    /**
     * SELECT ArticleMain
     */
    public List<ArticleMain> findArticles() {
        return articleRepository.getArticles();
    }

    public Optional<ArticleMain> findArticleByNumber(Long number) {
        return articleRepository.getArticleByNumber(number);
    }

    public Optional<ArticleMain> findArticleByName(String name) {
        return articleRepository.getArticleByName(name);
    }

    public Optional<ArticleMain> findArticleByNumberOrName(String numberOrName) {
        return NUMBER_REGEX_PATTERN.matcher(numberOrName).matches() ?
                findArticleByNumber(Long.parseLong(numberOrName)) : findArticleByName(numberOrName);
    }

    public Optional<ArticleMain> findArticleByImagePath(String imagePath) {
        return articleRepository.getArticleByImagePath(imagePath);
    }

    /**
     * INSERT ArticleMain
     */
    @Transactional
    public List<ArticleMain> registerArticles(ArticleMain... articles) {
        List<ArticleMain> articleList = new ArrayList<>();
        for (ArticleMain article : articles) {
            articleList.add(ArticleMain.builder().article(article).number(registerArticle(article).getNumber()).build());
        }
        return articleList;
    }

    @Transactional
    public ArticleMain registerArticle(ArticleMain article) {
        duplicateCheck(article);
        return ArticleMain.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE ArticleMain
     */
    @Transactional
    public void correctArticle(ArticleMain article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE ArticleMain
     */
    @Transactional
    public void removeArticleByName(String name) {
        existentCheck(name);
        articleRepository.deleteArticleByName(name);
    }

    /**
     * Other private methods
     */
    private void duplicateCheck(ArticleMain article) {
        articleRepository.getArticleByName(article.getName()).ifPresent(
                v -> {throw new AlreadyExistException(ALREADY_EXIST_ARTICLE_MAIN_NAME);}
        );
    }

    private void existentCheck(String name) {
        articleRepository.getArticleByName(name).orElseThrow(
                () -> new NotFoundException(NO_ARTICLE_MAIN_WITH_THAT_NAME)
        );
    }
}
