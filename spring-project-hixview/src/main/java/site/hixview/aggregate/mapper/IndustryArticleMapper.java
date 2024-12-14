package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
import site.hixview.aggregate.mapper.support.IndustryArticleMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IndustryArticleMapper extends IndustryArticleMapperSupport {
    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDomain")
    @Mapping(target = INDUSTRY_ARTICLE, ignore = true)
    IndustryArticle toIndustryArticle(IndustryArticleDto industryArticleDto);

    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDto")
    IndustryArticleDto toIndustryArticleDto(IndustryArticle industryArticle);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDomain")
    @Mapping(target = INDUSTRY_ARTICLE, ignore = true)
    IndustryArticle toIndustryArticle(IndustryArticleDtoNoNumber industryArticleDto);

    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDto")
    IndustryArticleDtoNoNumber toIndustryArticleDtoNoNumber(IndustryArticle industryArticle);
}
