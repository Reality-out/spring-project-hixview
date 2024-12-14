package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.dto.ArticleDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ArticleMapper {
    Article toArticle(ArticleDto articleDto);

    ArticleDto toArticleDto(Article article);
}
