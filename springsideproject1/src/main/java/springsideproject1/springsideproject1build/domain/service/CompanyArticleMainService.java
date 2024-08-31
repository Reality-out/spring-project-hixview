package springsideproject1.springsideproject1build.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleMain;
import springsideproject1.springsideproject1build.domain.error.AlreadyExistException;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;
import springsideproject1.springsideproject1build.domain.repository.CompanyArticleMainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.ALREADY_EXIST_ARTICLE_MAIN_NAME;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_ARTICLE_MAIN_WITH_THAT_NAME;
import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.NUMBER_REGEX_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyArticleMainService {

    private final CompanyArticleMainRepository articleRepository;

    /**
     * SELECT CompanyArticleMain
     */
    public List<CompanyArticleMain> findArticles() {
        return articleRepository.getArticles();
    }

    public Optional<CompanyArticleMain> findArticleByNumber(Long number) {
        return articleRepository.getArticleByNumber(number);
    }

    public Optional<CompanyArticleMain> findArticleByName(String name) {
        return articleRepository.getArticleByName(name);
    }

    public Optional<CompanyArticleMain> findArticleByNumberOrName(String numberOrName) {
        return NUMBER_REGEX_PATTERN.matcher(numberOrName).matches() ?
                findArticleByNumber(Long.parseLong(numberOrName)) : findArticleByName(numberOrName);
    }

    /**
     * INSERT CompanyArticleMain
     */
    @Transactional
    public List<CompanyArticleMain> registerArticles(CompanyArticleMain... articles) {
        List<CompanyArticleMain> articleList = new ArrayList<>();
        for (CompanyArticleMain article : articles) {
            articleList.add(CompanyArticleMain.builder().article(article).number(registerArticle(article).getNumber()).build());
        }
        return articleList;
    }

    @Transactional
    public CompanyArticleMain registerArticle(CompanyArticleMain article) {
        duplicateCheck(article);
        return CompanyArticleMain.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE CompanyArticleMain
     */
    @Transactional
    public void correctArticle(CompanyArticleMain article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE CompanyArticleMain
     */
    @Transactional
    public void removeArticleByName(String name) {
        existentCheck(name);
        articleRepository.deleteArticleByName(name);
    }

    /**
     * Other private methods
     */
    private void duplicateCheck(CompanyArticleMain article) {
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
