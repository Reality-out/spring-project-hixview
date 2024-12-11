package site.hixview.aggregate.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.domain.EconomyArticle.EconomyArticleBuilder;
import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE;

public class EconomyArticleFacade {
    public EconomyArticleBuilder createBuilder(EconomyArticle economyArticle) {
        return EconomyArticle.builder()
                .number(economyArticle.getNumber())
                .name(economyArticle.getName())
                .link(economyArticle.getLink())
                .date(economyArticle.getDate())
                .subjectCountry(economyArticle.getSubjectCountry())
                .importance(economyArticle.getImportance())
                .summary(economyArticle.getSummary())
                .pressNumber(economyArticle.getPressNumber())
                .mappedEconomyContentNumbers(economyArticle.getMappedEconomyContentNumbers());
    }
    
    public EconomyArticleBuilder createBuilder(EconomyArticleDto economyArticleDto) {
        List<Long> mappedEconomyContentNumbers;
        try {
            Map<String, List<Long>> mappedEconomyContentNumbersMap = new ObjectMapper()
                    .readValue(economyArticleDto.getMappedEconomyContentNumbers(), new TypeReference<>() {
            });
            mappedEconomyContentNumbers = mappedEconomyContentNumbersMap.get(MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + economyArticleDto.getMappedEconomyContentNumbers());
        }
        return EconomyArticle.builder()
                .number(economyArticleDto.getNumber())
                .name(economyArticleDto.getName())
                .link(economyArticleDto.getLink())
                .date(convertFromStringToLocalDate(economyArticleDto.getDate()))
                .subjectCountry(Country.valueOf(economyArticleDto.getSubjectCountry()))
                .importance(Importance.valueOf(economyArticleDto.getImportance()))
                .summary(economyArticleDto.getSummary())
                .pressNumber(economyArticleDto.getPressNumber())
                .mappedEconomyContentNumbers(mappedEconomyContentNumbers);
    }

    public EconomyArticleBuilder createBuilder(EconomyArticleDtoNoNumber economyArticleDto) {
        List<Long> mappedEconomyContentNumbers;
        try {
            Map<String, List<Long>> mappedEconomyContentNumbersMap = new ObjectMapper()
                    .readValue(economyArticleDto.getMappedEconomyContentNumbers(), new TypeReference<>() {
                    });
            mappedEconomyContentNumbers = mappedEconomyContentNumbersMap.get(MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + economyArticleDto.getMappedEconomyContentNumbers());
        }
        return EconomyArticle.builder()
                .name(economyArticleDto.getName())
                .link(economyArticleDto.getLink())
                .date(convertFromStringToLocalDate(economyArticleDto.getDate()))
                .subjectCountry(Country.valueOf(economyArticleDto.getSubjectCountry()))
                .importance(Importance.valueOf(economyArticleDto.getImportance()))
                .summary(economyArticleDto.getSummary())
                .pressNumber(economyArticleDto.getPressNumber())
                .mappedEconomyContentNumbers(mappedEconomyContentNumbers);
    }
}
