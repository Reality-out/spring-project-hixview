package site.hixview.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.EconomyArticleRepository;

import java.util.ArrayList;
import java.util.List;

import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_ECONOMY_ARTICLE_NAME;
import static site.hixview.domain.vo.ExceptionMessage.NO_ECONOMY_ARTICLE_WITH_THAT_NAME;

@Service
@Transactional
public class EconomyArticleService extends ArticleService<EconomyArticle, EconomyArticleRepository>{

    @Autowired
    private EconomyArticleRepository articleRepository;

    public EconomyArticleService(EconomyArticleRepository articleRepository) {
        super(articleRepository);
    }

    /**
     * INSERT EconomyArticle
     */
    public List<EconomyArticle> registerArticles(EconomyArticle... articles) {
        List<EconomyArticle> articleList = new ArrayList<>();
        for (EconomyArticle article : articles) {
            articleList.add(EconomyArticle.builder().article(article).number(registerArticle(article).getNumber()).build());
        }
        return articleList;
    }

    public EconomyArticle registerArticle(EconomyArticle article) {
        duplicateCheck(article);
        return EconomyArticle.builder().article(article).number(articleRepository.saveArticle(article)).build();
    }

    /**
     * UPDATE EconomyArticle
     */
    public void correctArticle(EconomyArticle article) {
        existentCheck(article.getName());
        articleRepository.updateArticle(article);
    }

    /**
     * REMOVE EconomyArticle
     */
    public void removeArticleByName(String name) {
        existentCheck(name);
        articleRepository.deleteArticleByName(name);
    }

    /**
     * Other private methods
     */
    private void duplicateCheck(EconomyArticle article) {
        articleRepository.getArticleByName(article.getName()).ifPresent(
                v -> {throw new AlreadyExistException(ALREADY_EXIST_ECONOMY_ARTICLE_NAME);}
        );
    }

    private void existentCheck(String name) {
        articleRepository.getArticleByName(name).orElseThrow(
                () -> new NotFoundException(NO_ECONOMY_ARTICLE_WITH_THAT_NAME)
        );
    }
}
