package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.article.CompanyArticle;
import springsideproject1.springsideproject1build.error.AlreadyExistException;
import springsideproject1.springsideproject1build.error.NotFoundException;
import springsideproject1.springsideproject1build.repository.CompanyArticleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_MESSAGE.ALREADY_EXIST_ARTICLE_NAME;
import static springsideproject1.springsideproject1build.error.constant.EXCEPTION_MESSAGE.NO_ARTICLE_WITH_THAT_NAME;
import static springsideproject1.springsideproject1build.utility.MainUtils.isNumeric;

@Service
@RequiredArgsConstructor
public class CompanyArticleService {

    private final CompanyArticleRepository articleRepository;

    /**
     * SELECT CompanyArticle
     */
    public List<CompanyArticle> findArticles() {
        return articleRepository.getArticles();
    }

    public List<CompanyArticle> findArticlesByDate(LocalDate date) {
        return articleRepository.getArticlesByDate(date);
    }

    public List<CompanyArticle> findArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return articleRepository.getArticlesByDate(startDate, endDate);
    }

    public Optional<CompanyArticle> findArticleByNumber(Long number) {
        return articleRepository.getArticleByNumber(number);
    }

    public Optional<CompanyArticle> findArticleByName(String name) {
        return articleRepository.getArticleByName(name);
    }

    public Optional<CompanyArticle> findArticleByNumberOrName(String numberOrName) {
        return isNumeric(numberOrName) ? findArticleByNumber(Long.parseLong(numberOrName)) : findArticleByName(numberOrName);
    }

    /**
     * INSERT CompanyArticle
     */
    @Transactional
    public List<CompanyArticle> registerArticles(CompanyArticle... articles) {
        List<CompanyArticle> articleList = new ArrayList<>();
        for (CompanyArticle article : articles) {
            articleList.add(CompanyArticle.builder().article(article).number(registerArticle(article).getNumber()).build());
        }
        return articleList;
    }

    @Transactional
    public CompanyArticle registerArticle(CompanyArticle article) {
        duplicateCheck(article);
        return CompanyArticle.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE CompanyArticle
     */
    @Transactional
    public void correctArticle(CompanyArticle article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE CompanyArticle
     */
    @Transactional
    public void removeArticle(String name) {
        existentCheck(name);
        articleRepository.deleteArticleByName(name);
    }

    /**
     * Other private methods
     */
    @Transactional
    private void duplicateCheck(CompanyArticle article) {
        articleRepository.getArticleByName(article.getName()).ifPresent(
                v -> {throw new AlreadyExistException(ALREADY_EXIST_ARTICLE_NAME);}
        );
    }

    @Transactional
    private void existentCheck(String name) {
        articleRepository.getArticleByName(name).orElseThrow(
                () -> new NotFoundException(NO_ARTICLE_WITH_THAT_NAME)
        );
    }
}
