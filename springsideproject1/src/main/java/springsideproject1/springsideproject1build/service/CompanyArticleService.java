package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.repository.CompanyArticleRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static springsideproject1.springsideproject1build.Utility.encodeUTF8;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_ARTICLE_NAME;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_ARTICLE_WITH_THAT_NAME;

@Service
@RequiredArgsConstructor
public class CompanyArticleService {

    private final CompanyArticleRepository articleRepository;

    /**
     * SELECT Articles
     */
    public List<CompanyArticle> findArticles() {
        return articleRepository.findAllArticles();
    }

    public List<CompanyArticle> SearchOneArticleByDate(LocalDate articleDate) {
        return articleRepository.searchArticlesByDate(articleDate);
    }

    public List<CompanyArticle> SearchOneArticleByDate(LocalDate articleStartDate, LocalDate articleEndDate) {
        return articleRepository.searchArticlesByDate(articleStartDate, articleEndDate);
    }

    /**
     * SELECT One CompanyArticle
     */
    public Optional<CompanyArticle> SearchOneArticleByName(String articleName) {
        return articleRepository.searchArticleByName(articleName);
    }

    /**
     * INSERT One CompanyArticle
     */
    @Transactional
    public CompanyArticle joinArticle(CompanyArticle article) {
        duplicateCodeCheck(article);

        return new CompanyArticle.ArticleBuilder()
                .article(article)
                .number(articleRepository.saveOneArticle(article))
                .build();
    }

    @Transactional
    public List<String> joinArticlesWithString(String subjectCompany, String articleString, String linkString) {
        List<List<String>> partialArticleLists = parseArticleString(articleString);
        List<String> linkList = parseLinkString(linkString);

        for (int i = 0; i < linkList.size(); i++){
            List<String> partialArticle = partialArticleLists.get(i);
            joinArticle(new CompanyArticle.ArticleBuilder()
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

    /**
     * REMOVE One CompanyArticle
     */
    @Transactional
    public void removeArticle(String articleName) {
        articleRepository.searchArticleByName(articleName).orElseThrow(
                () -> new IllegalStateException(NO_ARTICLE_WITH_THAT_NAME)
        );

        articleRepository.removeArticleByName(articleName);
    }

    /**
     * Other private methods
     */
    @Transactional
    private void duplicateCodeCheck(CompanyArticle article) {
        articleRepository.searchArticleByName(article.getName()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_ARTICLE_NAME);}
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
