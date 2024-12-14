package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;

import static site.hixview.aggregate.vo.WordCamel.INDUSTRY_ARTICLE_SECOND_CATEGORY;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndustryArticleSecondCategoryMapper {
    @Mapping(target = INDUSTRY_ARTICLE_SECOND_CATEGORY, ignore = true)
    IndustryArticleSecondCategory toIndustryArticleSecondCategory(IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto);

    IndustryArticleSecondCategoryDto toIndustryArticleSecondCategoryDto(IndustryArticleSecondCategory industryArticleSecondCategory);
}
