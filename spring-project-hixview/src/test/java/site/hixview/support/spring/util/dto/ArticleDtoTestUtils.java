package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.ArticleDto;
import site.hixview.support.spring.util.ArticleTestUtils;

public interface ArticleDtoTestUtils extends ArticleTestUtils {
    /**
     * Create
     */
    default ArticleDto createArticleDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setNumber(article.getNumber());
        return articleDto;
    }

    default ArticleDto createAnotherArticleDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setNumber(anotherArticle.getNumber());
        return articleDto;
    }
}
