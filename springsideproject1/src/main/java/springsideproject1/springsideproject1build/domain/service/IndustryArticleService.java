package springsideproject1.springsideproject1build.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.error.AlreadyExistException;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;
import springsideproject1.springsideproject1build.domain.repository.IndustryArticleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.*;
import static springsideproject1.springsideproject1build.domain.vo.REGEX.NUMBER_REGEX_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndustryArticleService {

    private final IndustryArticleRepository articleRepository;

    /**
     * SELECT IndustryArticle
     */
    public List<IndustryArticle> findArticles() {
        return articleRepository.getArticles();
    }

    public List<IndustryArticle> findArticlesByDate(LocalDate date) {
        return articleRepository.getArticlesByDate(date);
    }

    public List<IndustryArticle> findArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return articleRepository.getArticlesByDate(startDate, endDate);
    }

    public List<IndustryArticle> findLatestArticles() {
        return articleRepository.getLatestArticles();
    }

    public Optional<IndustryArticle> findArticleByNumber(Long number) {
        return articleRepository.getArticleByNumber(number);
    }

    public Optional<IndustryArticle> findArticleByName(String name) {
        return articleRepository.getArticleByName(name);
    }

    public Optional<IndustryArticle> findArticleByNumberOrName(String numberOrName) {
        return NUMBER_REGEX_PATTERN.matcher(numberOrName).matches() ?
                findArticleByNumber(Long.parseLong(numberOrName)) : findArticleByName(numberOrName);
    }

    public Optional<IndustryArticle> findArticleByLink(String link) {
        return articleRepository.getArticleByLink(link);
    }

    /**
     * INSERT IndustryArticle
     */
    @Transactional
    public List<IndustryArticle> registerArticles(IndustryArticle... articles) {
        List<IndustryArticle> articleList = new ArrayList<>();
        for (IndustryArticle article : articles) {
            articleList.add(IndustryArticle.builder().article(article).number(registerArticle(article).getNumber()).build());
        }
        return articleList;
    }

    @Transactional
    public IndustryArticle registerArticle(IndustryArticle article) {
        duplicateCheck(article);
        return IndustryArticle.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE IndustryArticle
     */
    @Transactional
    public void correctArticle(IndustryArticle article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE IndustryArticle
     */
    @Transactional
    public void removeArticleByName(String name) {
        existentCheck(name);
        articleRepository.deleteArticleByName(name);
    }

    /**
     * Other private methods
     */
    private void duplicateCheck(IndustryArticle article) {
        articleRepository.getArticleByName(article.getName()).ifPresent(
                v -> {throw new AlreadyExistException(ALREADY_EXIST_INDUSTRY_ARTICLE_NAME);}
        );
    }

    private void existentCheck(String name) {
        articleRepository.getArticleByName(name).orElseThrow(
                () -> new NotFoundException(NO_INDUSTRY_ARTICLE_WITH_THAT_NAME)
        );
    }
}
