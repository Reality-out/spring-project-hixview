package site.hixview.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.IndustryArticleRepository;

import java.util.ArrayList;
import java.util.List;

import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_INDUSTRY_ARTICLE_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_INDUSTRY_ARTICLE_WITH_THAT_NAME;

@Service
@Transactional
public class IndustryArticleService extends ArticleService<IndustryArticle, IndustryArticleRepository>{

    @Autowired
    private IndustryArticleRepository articleRepository;

    public IndustryArticleService(IndustryArticleRepository articleRepository) {
        super(articleRepository);
    }

    /**
     * INSERT IndustryArticle
     */
    public List<IndustryArticle> registerArticles(IndustryArticle... articles) {
        List<IndustryArticle> articleList = new ArrayList<>();
        for (IndustryArticle article : articles) {
            articleList.add(IndustryArticle.builder().article(article).number(registerArticle(article).getNumber()).build());
        }
        return articleList;
    }

    public IndustryArticle registerArticle(IndustryArticle article) {
        duplicateCheck(article);
        return IndustryArticle.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE IndustryArticle
     */
    public void correctArticle(IndustryArticle article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE IndustryArticle
     */
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
