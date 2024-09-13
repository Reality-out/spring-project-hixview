package springsideproject1.springsideproject1build.domain.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.Article;
import springsideproject1.springsideproject1build.domain.repository.ArticleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.domain.vo.REGEX.NUMBER_REGEX_PATTERN;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public abstract class ArticleService<T extends Article, U extends ArticleRepository<T>> {

    private final U articleRepository;

    /**
     * SELECT T extends Article
     */
    public List<T> findArticles() {
        return articleRepository.getArticles();
    }

    public List<T> findArticlesByDate(LocalDate date) {
        return articleRepository.getArticlesByDate(date);
    }

    public List<T> findArticlesByDate(LocalDate startDate, LocalDate endDate) {
        return articleRepository.getArticlesByDate(startDate, endDate);
    }

    public List<T> findLatestArticles() {
        return articleRepository.getLatestArticles();
    }

    public Optional<T> findArticleByNumber(Long number) {
        return articleRepository.getArticleByNumber(number);
    }

    public Optional<T> findArticleByName(String name) {
        return articleRepository.getArticleByName(name);
    }

    public Optional<T> findArticleByNumberOrName(String numberOrName) {
        return NUMBER_REGEX_PATTERN.matcher(numberOrName).matches() ?
                findArticleByNumber(Long.parseLong(numberOrName)) : findArticleByName(numberOrName);
    }

    public Optional<T> findArticleByLink(String link) {
        return articleRepository.getArticleByLink(link);
    }
}
