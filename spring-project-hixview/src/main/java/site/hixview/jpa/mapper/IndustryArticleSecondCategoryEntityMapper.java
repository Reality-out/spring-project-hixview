package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.mapper.support.IndustryArticleSecondCategoryEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class IndustryArticleSecondCategoryEntityMapper extends IndustryArticleSecondCategoryEntityMapperSupport {
    @Mapping(target = INDUSTRY_ARTICLE, ignore = true)
    @Mapping(target = SECOND_CATEGORY, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    public abstract IndustryArticleSecondCategoryEntity toIndustryArticleSecondCategoryEntity(IndustryArticleSecondCategory industryArticleSecondCategory);

    @Mapping(source = INDUSTRY_ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(source = SECOND_CATEGORY, target = SECOND_CATEGORY_NUMBER, qualifiedByName = "secondCategoryNumberToDomain")
    public abstract IndustryArticleSecondCategory toIndustryArticleSecondCategory(IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity);
}
