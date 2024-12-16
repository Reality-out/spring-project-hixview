package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.mapper.support.IndustryArticleSecondCategoryEntityMapperSupport;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface IndustryArticleSecondCategoryEntityMapper extends IndustryArticleSecondCategoryEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    IndustryArticleSecondCategoryEntity toIndustryArticleSecondCategoryEntity(IndustryArticleSecondCategory industryArticleSecondCategory,
                                                                              @Context IndustryArticleEntityRepository industryArticleRepository,
                                                                              @Context SecondCategoryEntityRepository secondCategoryRepository);

    @Mapping(source = INDUSTRY_ARTICLE, target = ARTICLE_NUMBER, qualifiedByName = "articleNumberToDomain")
    @Mapping(source = SECOND_CATEGORY, target = SECOND_CATEGORY_NUMBER, qualifiedByName = "secondCategoryNumberToDomain")
    @Mapping(target = INDUSTRY_ARTICLE_SECOND_CATEGORY, ignore = true)
    IndustryArticleSecondCategory toIndustryArticleSecondCategory(IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity);
}
