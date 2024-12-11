package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.dto.ArticleDto;

@Mapper
public interface ArticleMapper {
    Article toArticle(ArticleDto articleDto);

    ArticleDto toArticleDto(Article article);
}
