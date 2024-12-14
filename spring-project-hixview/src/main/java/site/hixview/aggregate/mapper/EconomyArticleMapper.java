package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;
import site.hixview.aggregate.mapper.support.EconomyArticleMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EconomyArticleMapper extends EconomyArticleMapperSupport {
    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDomain")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDomain")
    @Mapping(source = MAPPED_ECONOMY_CONTENT_NUMBERS, target = MAPPED_ECONOMY_CONTENT_NUMBERS, qualifiedByName = "mappedEconomyContentNumbersToDomain")
    @Mapping(target = ECONOMY_ARTICLE, ignore = true)
    EconomyArticle toEconomyArticle(EconomyArticleDto economyArticleDto);

    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDto")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDto")
    @Mapping(source = MAPPED_ECONOMY_CONTENT_NUMBERS, target = MAPPED_ECONOMY_CONTENT_NUMBERS, qualifiedByName = "mappedEconomyContentNumbersToDto")
    EconomyArticleDto toEconomyArticleDto(EconomyArticle economyArticle);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDomain")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDomain")
    @Mapping(source = MAPPED_ECONOMY_CONTENT_NUMBERS, target = MAPPED_ECONOMY_CONTENT_NUMBERS, qualifiedByName = "mappedEconomyContentNumbersToDomain")
    @Mapping(target = ECONOMY_ARTICLE, ignore = true)
    EconomyArticle toEconomyArticle(EconomyArticleDtoNoNumber economyArticleDto);

    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDto")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDto")
    @Mapping(source = MAPPED_ECONOMY_CONTENT_NUMBERS, target = MAPPED_ECONOMY_CONTENT_NUMBERS, qualifiedByName = "mappedEconomyContentNumbersToDto")
    EconomyArticleDtoNoNumber toEconomyArticleDtoNoNumber(EconomyArticle economyArticle);
}
