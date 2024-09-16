package site.hixview.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.CompanyArticleRepository;

import java.util.ArrayList;
import java.util.List;

import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_COMPANY_ARTICLE_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_COMPANY_ARTICLE_WITH_THAT_NAME;

@Service
@Transactional
public class CompanyArticleService extends ArticleService<CompanyArticle, CompanyArticleRepository>{

    @Autowired
    private CompanyArticleRepository articleRepository;

    public CompanyArticleService(CompanyArticleRepository articleRepository) {
        super(articleRepository);
    }

    /**
     * INSERT CompanyArticle
     */
    public List<CompanyArticle> registerArticles(CompanyArticle... articles) {
        List<CompanyArticle> articleList = new ArrayList<>();
        for (CompanyArticle article : articles) {
            articleList.add(CompanyArticle.builder().article(article).number(registerArticle(article).getNumber()).build());
        }
        return articleList;
    }

    public CompanyArticle registerArticle(CompanyArticle article) {
        duplicateCheck(article);
        return CompanyArticle.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE CompanyArticle
     */
    public void correctArticle(CompanyArticle article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE CompanyArticle
     */
    public void removeArticleByName(String name) {
        existentCheck(name);
        articleRepository.deleteArticleByName(name);
    }

    /**
     * Other private methods
     */
    private void duplicateCheck(CompanyArticle article) {
        articleRepository.getArticleByName(article.getName()).ifPresent(
                v -> {throw new AlreadyExistException(ALREADY_EXIST_COMPANY_ARTICLE_NAME);}
        );
    }

    private void existentCheck(String name) {
        articleRepository.getArticleByName(name).orElseThrow(
                () -> new NotFoundException(NO_COMPANY_ARTICLE_WITH_THAT_NAME)
        );
    }
}
