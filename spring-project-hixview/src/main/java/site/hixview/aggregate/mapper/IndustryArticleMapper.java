package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
import site.hixview.aggregate.mapper.support.IndustryArticleMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper
public interface IndustryArticleMapper extends IndustryArticleMapperSupport {
    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDomain")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDomain")
    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDomain")
    IndustryArticle toIndustryArticle(IndustryArticleDto industryArticleDto);

    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDto")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDto")
    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDto")
    IndustryArticleDto toIndustryArticleDto(IndustryArticle industryArticle);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDomain")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDomain")
    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDomain")
    IndustryArticle toIndustryArticle(IndustryArticleDtoNoNumber industryArticleDto);

    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDto")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDto")
    @Mapping(source = MAPPED_SECOND_CATEGORY_NUMBERS, target = MAPPED_SECOND_CATEGORY_NUMBERS, qualifiedByName = "mappedSecondCategoryNumbersToDto")
    IndustryArticleDtoNoNumber toIndustryArticleDtoNoNumber(IndustryArticle industryArticle);
}
