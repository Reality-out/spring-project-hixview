package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;
import site.hixview.aggregate.mapper.support.CompanyArticleMapperSupport;

import static site.hixview.aggregate.vo.WordCamel.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CompanyArticleMapper extends CompanyArticleMapperSupport {
    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDomain")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDomain")
    @Mapping(source = MAPPED_COMPANY_CODES, target = MAPPED_COMPANY_CODES, qualifiedByName = "mappedEconomyContentNumbersToDomain")
    @Mapping(target = COMPANY_ARTICLE, ignore = true)
    CompanyArticle toCompanyArticle(CompanyArticleDto companyArticleDto);

    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDto")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDto")
    @Mapping(source = MAPPED_COMPANY_CODES, target = MAPPED_COMPANY_CODES, qualifiedByName = "mappedEconomyContentNumbersToDto")
    CompanyArticleDto toCompanyArticleDto(CompanyArticle companyArticle);

    @Mapping(target = NUMBER, ignore = true)
    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDomain")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDomain")
    @Mapping(source = MAPPED_COMPANY_CODES, target = MAPPED_COMPANY_CODES, qualifiedByName = "mappedEconomyContentNumbersToDomain")
    @Mapping(target = COMPANY_ARTICLE, ignore = true)
    CompanyArticle toCompanyArticle(CompanyArticleDtoNoNumber companyArticleDto);

    @Mapping(source = SUBJECT_COUNTRY, target = SUBJECT_COUNTRY, qualifiedByName = "subjectCountryToDto")
    @Mapping(source = IMPORTANCE, target = IMPORTANCE, qualifiedByName = "importanceToDto")
    @Mapping(source = MAPPED_COMPANY_CODES, target = MAPPED_COMPANY_CODES, qualifiedByName = "mappedEconomyContentNumbersToDto")
    CompanyArticleDtoNoNumber toCompanyArticleDtoNoNumber(CompanyArticle companyArticle);
}
