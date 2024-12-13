package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.ArticleDto;

public interface ArticleDtoTestUtils {
    /**
     * Create
     */
    default ArticleDto createArticleDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setNumber(1L);
        return articleDto;
    }

    default ArticleDto createAnotherArticleDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setNumber(2L);
        return articleDto;
    }
}
