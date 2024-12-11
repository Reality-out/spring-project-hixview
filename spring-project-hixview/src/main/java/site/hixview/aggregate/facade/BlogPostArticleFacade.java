package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.domain.BlogPostArticle.BlogPostArticleBuilder;
import site.hixview.aggregate.dto.BlogPostArticleDto;

public class BlogPostArticleFacade {
    public BlogPostArticleBuilder createBuilder(BlogPostArticle mapper) {
        return BlogPostArticle.builder()
                .number(mapper.getNumber())
                .postNumber(mapper.getPostNumber())
                .articleNumber(mapper.getArticleNumber());
    }

    public BlogPostArticleBuilder createBuilder(BlogPostArticleDto mapperDto) {
        return BlogPostArticle.builder()
                .number(mapperDto.getNumber())
                .postNumber(mapperDto.getPostNumber())
                .articleNumber(mapperDto.getArticleNumber());
    }
}
