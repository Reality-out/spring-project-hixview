package site.hixview.jpa.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.mapper.support.IndustryArticleSecondCategoryEntityMapperSupport;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndustryArticleSecondCategoryEntityMapper extends IndustryArticleSecondCategoryEntityMapperSupport {
    @Mapping(target = INDUSTRY_ARTICLE, ignore = true)
    @Mapping(target = SECOND_CATEGORY, ignore = true)
    @Mapping(target = VERSION_NUMBER, ignore = true)
    IndustryArticleSecondCategoryEntity toIndustryArticleSecondCategoryEntity(IndustryArticleSecondCategory industryArticleSecondCategory,
                                                                              @Context IndustryArticleEntityRepository industryArticleRepository,
                                                                              @Context SecondCategoryEntityRepository secondCategoryRepository);

    @Mapping(source = INDUSTRY_ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(source = SECOND_CATEGORY, target = SECOND_CATEGORY_NUMBER, qualifiedByName = "secondCategoryNumberToDomain")
    IndustryArticleSecondCategory toIndustryArticleSecondCategory(IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity);
}
