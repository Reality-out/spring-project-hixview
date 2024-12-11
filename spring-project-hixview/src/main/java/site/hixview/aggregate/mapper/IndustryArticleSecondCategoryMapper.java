package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;

@Mapper
public interface IndustryArticleSecondCategoryMapper {
    IndustryArticleSecondCategory toIndustryArticleSecondCategory(IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto);

    IndustryArticleSecondCategoryDto toIndustryArticleSecondCategoryDto(IndustryArticleSecondCategory industryArticleSecondCategory);
}
