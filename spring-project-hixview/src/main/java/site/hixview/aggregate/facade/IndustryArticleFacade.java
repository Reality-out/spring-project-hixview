package site.hixview.aggregate.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.domain.IndustryArticle.IndustryArticleBuilder;
import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE;

public class IndustryArticleFacade {
    public IndustryArticleBuilder createBuilder(IndustryArticle industryArticle) {
        return IndustryArticle.builder()
                .number(industryArticle.getNumber())
                .name(industryArticle.getName())
                .link(industryArticle.getLink())
                .date(industryArticle.getDate())
                .subjectCountry(industryArticle.getSubjectCountry())
                .importance(industryArticle.getImportance())
                .summary(industryArticle.getSummary())
                .pressNumber(industryArticle.getPressNumber())
                .mappedSecondCategoryNumbers(industryArticle.getMappedSecondCategoryNumbers());
    }

    public IndustryArticleBuilder createBuilder(IndustryArticleDto industryArticleDto) {
        List<Long> mappedSecondCategoryNumbers;
        try {
            Map<String, List<Long>> mappedSecondCategoryNumbersMap = new ObjectMapper()
                    .readValue(industryArticleDto.getMappedSecondCategoryNumbers(), new TypeReference<>() {
            });
            mappedSecondCategoryNumbers = mappedSecondCategoryNumbersMap.get(MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + industryArticleDto.getMappedSecondCategoryNumbers());
        }
        return IndustryArticle.builder()
                .number(industryArticleDto.getNumber())
                .name(industryArticleDto.getName())
                .link(industryArticleDto.getLink())
                .date(convertFromStringToLocalDate(industryArticleDto.getDate()))
                .subjectCountry(Country.valueOf(industryArticleDto.getSubjectCountry()))
                .importance(Importance.valueOf(industryArticleDto.getImportance()))
                .summary(industryArticleDto.getSummary())
                .pressNumber(industryArticleDto.getPressNumber())
                .mappedSecondCategoryNumbers(mappedSecondCategoryNumbers);
    }

    public IndustryArticleBuilder createBuilder(IndustryArticleDtoNoNumber industryArticleDto) {
        List<Long> mappedSecondCategoryNumbers;
        try {
            Map<String, List<Long>> mappedSecondCategoryNumbersMap = new ObjectMapper()
                    .readValue(industryArticleDto.getMappedSecondCategoryNumbers(), new TypeReference<>() {
                    });
            mappedSecondCategoryNumbers = mappedSecondCategoryNumbersMap.get(MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + industryArticleDto.getMappedSecondCategoryNumbers());
        }
        return IndustryArticle.builder()
                .name(industryArticleDto.getName())
                .link(industryArticleDto.getLink())
                .date(convertFromStringToLocalDate(industryArticleDto.getDate()))
                .subjectCountry(Country.valueOf(industryArticleDto.getSubjectCountry()))
                .importance(Importance.valueOf(industryArticleDto.getImportance()))
                .summary(industryArticleDto.getSummary())
                .pressNumber(industryArticleDto.getPressNumber())
                .mappedSecondCategoryNumbers(mappedSecondCategoryNumbers);
    }
}
