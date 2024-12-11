package site.hixview.aggregate.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.domain.CompanyArticle.CompanyArticleBuilder;
import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_COMPANY_CODES_SNAKE;

public class CompanyArticleFacade {
    public CompanyArticleBuilder createBuilder(CompanyArticle companyArticle) {
        return CompanyArticle.builder()
                .number(companyArticle.getNumber())
                .name(companyArticle.getName())
                .link(companyArticle.getLink())
                .date(companyArticle.getDate())
                .subjectCountry(companyArticle.getSubjectCountry())
                .importance(companyArticle.getImportance())
                .summary(companyArticle.getSummary())
                .pressNumber(companyArticle.getPressNumber())
                .mappedCompanyCodes(companyArticle.getMappedCompanyCodes());
    }

    public CompanyArticleBuilder createBuilder(CompanyArticleDto companyArticleDto) {
        List<String> mappedCompanyCodes;
        try {
            Map<String, List<String>> mappedCompanyCodesMap = new ObjectMapper()
                    .readValue(companyArticleDto.getMappedCompanyCodes(), new TypeReference<>() {
            });
            mappedCompanyCodes = mappedCompanyCodesMap.get(MAPPED_COMPANY_CODES_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + companyArticleDto.getMappedCompanyCodes());
        }
        return CompanyArticle.builder()
                .number(companyArticleDto.getNumber())
                .name(companyArticleDto.getName())
                .link(companyArticleDto.getLink())
                .date(convertFromStringToLocalDate(companyArticleDto.getDate()))
                .subjectCountry(Country.valueOf(companyArticleDto.getSubjectCountry()))
                .importance(Importance.valueOf(companyArticleDto.getImportance()))
                .summary(companyArticleDto.getSummary())
                .pressNumber(companyArticleDto.getPressNumber())
                .mappedCompanyCodes(mappedCompanyCodes);
    }

    public CompanyArticleBuilder createBuilder(CompanyArticleDtoNoNumber companyArticleDto) {
        List<String> mappedCompanyCodes;
        try {
            Map<String, List<String>> mappedCompanyCodesMap = new ObjectMapper()
                    .readValue(companyArticleDto.getMappedCompanyCodes(), new TypeReference<>() {
                    });
            mappedCompanyCodes = mappedCompanyCodesMap.get(MAPPED_COMPANY_CODES_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + companyArticleDto.getMappedCompanyCodes());
        }
        return CompanyArticle.builder()
                .name(companyArticleDto.getName())
                .link(companyArticleDto.getLink())
                .date(convertFromStringToLocalDate(companyArticleDto.getDate()))
                .subjectCountry(Country.valueOf(companyArticleDto.getSubjectCountry()))
                .importance(Importance.valueOf(companyArticleDto.getImportance()))
                .summary(companyArticleDto.getSummary())
                .pressNumber(companyArticleDto.getPressNumber())
                .mappedCompanyCodes(mappedCompanyCodes);
    }
}
