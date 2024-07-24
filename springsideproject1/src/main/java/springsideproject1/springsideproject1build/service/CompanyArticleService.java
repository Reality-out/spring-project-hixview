package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.repository.CompanyArticleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

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
    public void joinArticle(CompanyArticle article) {
        duplicateCodeCheck(article);

        //noinspection ReassignedVariable
        article = new CompanyArticle.ArticleBuilder()
                .article(article)
                .number(articleRepository.saveOneArticle(article))
                .build();
    }

    @Transactional
    public void joinArticlesWithString(String subjectCompany, String articleString, String linkString) {
        List<List<String>> partialArticleLists = parseArticleString(articleString);
        List<String> linkLists = parseLinkString(linkString);

        for (int i = 0; i < linkLists.size(); i++){
            joinArticle(new CompanyArticle.ArticleBuilder()
                    .name(partialArticleLists.get(i).get(0))
                    .press(partialArticleLists.get(i).get(4))
                    .subjectCompany(subjectCompany)
                    .link(linkLists.get(i))
                    .date(LocalDate.of(parseInt(partialArticleLists.get(i).get(1)),
                            parseInt(partialArticleLists.get(i).get(2)),
                            parseInt(partialArticleLists.get(i).get(3))))
                    .importance(0)
                    .build());
        }
    }

    /**
     * REMOVE One CompanyArticle
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
    private void duplicateCodeCheck(CompanyArticle article) {
        articleRepository.searchArticleByName(article.getName()).ifPresent(
                v -> {throw new IllegalStateException("이미 존재하는 기사 제목입니다.");}
        );
    }

    @Transactional
    private List<List<String>> parseArticleString(String articleString) {
        List<String> dividedArticle = List.of(articleString.split("\\\\n"));
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

    @Transactional
    private List<String> parseLinkString(String linkString) {
        return List.of(linkString.split("\\\\n"));
    }
}
