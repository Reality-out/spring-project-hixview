package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.repository.CompanyArticleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static springsideproject1.springsideproject1build.utility.MainUtility.encodeUTF8;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_ARTICLE_NAME;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_ARTICLE_WITH_THAT_NAME;

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

    public List<CompanyArticle> findArticlesByDate(LocalDate articleDate) {
        return articleRepository.getArticlesByDate(articleDate);
    }

    public List<CompanyArticle> findArticlesByDate(LocalDate articleStartDate, LocalDate articleEndDate) {
        return articleRepository.getArticlesByDate(articleStartDate, articleEndDate);
    }

    public Optional<CompanyArticle> findArticleByName(String articleName) {
        return articleRepository.getArticleByName(articleName);
    }

    /**
     * INSERT CompanyArticle
     */
    @Transactional
    public List<String> joinArticlesWithString(String subjectCompany, String articleString, String linkString) {
        List<List<String>> partialArticleLists = parseArticleString(articleString);
        List<String> linkList = parseLinkString(linkString);

        for (int i = 0; i < linkList.size(); i++){
            List<String> partialArticle = partialArticleLists.get(i);
            joinArticle(CompanyArticle.builder()
                    .name(partialArticle.get(0))
                    .press(partialArticle.get(4))
                    .subjectCompany(subjectCompany)
                    .link(linkList.get(i))
                    .date(LocalDate.of(parseInt(partialArticle.get(1)),
                            parseInt(partialArticle.get(2)),
                            parseInt(partialArticle.get(3))))
                    .importance(0)
                    .build());
        }

        return encodeUTF8(partialArticleLists.stream().map(List::getFirst).collect(Collectors.toList()));
    }

    @Transactional
    public CompanyArticle joinArticle(CompanyArticle article) {
        duplicateCheck(article);
        return CompanyArticle.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE CompanyArticle
     */
    @Transactional
    public void updateArticle(CompanyArticle article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE CompanyArticle
     */
    @Transactional
    public void removeArticle(String articleName) {
        existentCheck(articleName);
        articleRepository.deleteArticleByName(articleName);
    }

    /**
     * Other private methods
     */
    @Transactional
    private void duplicateCheck(CompanyArticle article) {
        articleRepository.getArticleByName(article.getName()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_ARTICLE_NAME);}
        );
    }

    @Transactional
    private void existentCheck(String articleName) {
        articleRepository.getArticleByName(articleName).orElseThrow(
                () -> new IllegalStateException(NO_ARTICLE_WITH_THAT_NAME)
        );
    }

    @Transactional
    private List<List<String>> parseArticleString(String articleString) {
        List<String> dividedArticle = List.of(articleString.split("\\R"));
        List<List<String>> returnArticle = new ArrayList<>();

        for (int i = 0; i < dividedArticle.size(); i++) {
            if (i % 2 == 0) {
                returnArticle.add(new ArrayList<>(List.of(dividedArticle.get(i))));
            } else {
                returnArticle.getLast().addAll(Arrays.asList(dividedArticle.get(i)
                        .replaceAll("^\\(|\\)$", "").split(",\\s|-")));
            }
        }
        return returnArticle;
    }

    @Transactional
    private List<String> parseLinkString(String linkString) {
        return List.of(linkString.split("\\R"));
    }
}
