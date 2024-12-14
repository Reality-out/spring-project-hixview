package site.hixview.jpa.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.mapper.support.IndustryArticleEntityMapperSupport;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryArticleSecondCategoryEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndustryArticleEntityMapper extends IndustryArticleEntityMapperSupport {
    @Mapping(target = ARTICLE, ignore = true)
    @Mapping(target = PRESS, ignore = true)
    @Mapping(target = INDUSTRY_ARTICLE, ignore = true)
    @Mapping(target = FIRST_CATEGORY, ignore = true)
    IndustryArticleEntity toIndustryArticleEntity(IndustryArticle industryArticle,
                                                  @Context ArticleEntityRepository articleEntityRepository,
                                                  @Context PressEntityRepository pressEntityRepository,
                                                  @Context FirstCategoryEntityRepository firstCategoryRepository);

    @Mapping(source = ARTICLE, target = NUMBER, qualifiedByName = "numberToDomain")
    @Mapping(source = PRESS, target = PRESS_NUMBER, qualifiedByName = "pressNumberToDomain")
    @Mapping(source = FIRST_CATEGORY, target = FIRST_CATEGORY_NUMBER, qualifiedByName = "firstCategoryNumberToDomain")
    @Mapping(target = MAPPED_SECOND_CATEGORY_NUMBERS, ignore = true)
    @Mapping(target = INDUSTRY_ARTICLE, ignore = true)
    IndustryArticle toIndustryArticle(IndustryArticleEntity industryArticleEntity,
                                      @Context IndustryArticleSecondCategoryEntityRepository industryArticleSecondCategoryRepository);
}
