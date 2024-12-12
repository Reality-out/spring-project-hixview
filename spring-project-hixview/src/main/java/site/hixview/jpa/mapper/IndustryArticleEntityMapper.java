package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.mapper.support.IndustryArticleEntityMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class IndustryArticleEntityMapper extends IndustryArticleEntityMapperSupport {
    @Mapping(target = ARTICLE, ignore = true)
    @Mapping(target = PRESS, ignore = true)
    @Mapping(target = INDUSTRY_ARTICLE, ignore = true)
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    public abstract IndustryArticleEntity toIndustryArticleEntity(IndustryArticle industryArticle);

    @Mapping(source = ARTICLE, target = NUMBER, qualifiedByName = "numberToDomain")
    @Mapping(source = PRESS, target = PRESS_NUMBER, qualifiedByName = "pressNumberToDomain")
    @Mapping(source = FIRST_CATEGORY, target = FIRST_CATEGORY_NUMBER, qualifiedByName = "firstCategoryNumberToDomain")
    @Mapping(target = MAPPED_SECOND_CATEGORY_NUMBERS, ignore = true)
    public abstract IndustryArticle toIndustryArticle(IndustryArticleEntity industryArticleEntity);
}
