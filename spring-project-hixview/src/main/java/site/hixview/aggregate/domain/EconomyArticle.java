package site.hixview.aggregate.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_MAP_TO_JSON;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class EconomyArticle implements ConvertibleToWholeDto<EconomyArticleDto, EconomyArticleDtoNoNumber> {

    private Long number;
    private String name;
    private String link;
    private LocalDate date;
    private Country subjectCountry;
    private Importance importance;
    private String summary;
    private Long pressNumber;
    private final List<Long> mappedEconomyContentNumbers;

    @Override
    public EconomyArticleDto toDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setNumber(number);
        economyArticleDto.setName(name);
        economyArticleDto.setLink(link);
        economyArticleDto.setDate(String.valueOf(date));
        economyArticleDto.setSubjectCountry(subjectCountry.name());
        economyArticleDto.setImportance(importance.name());
        economyArticleDto.setSummary(summary);
        economyArticleDto.setPressNumber(pressNumber);
        try {
            economyArticleDto.setMappedEconomyContentNumbers(new ObjectMapper().writeValueAsString(new HashMap<>(){{
                put(MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE, mappedEconomyContentNumbers);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + mappedEconomyContentNumbers);
        }
        return economyArticleDto;
    }

    @Override
    public EconomyArticleDtoNoNumber toDtoNoNumber() {
        EconomyArticleDtoNoNumber economyArticleDto = new EconomyArticleDtoNoNumber();
        economyArticleDto.setName(name);
        economyArticleDto.setLink(link);
        economyArticleDto.setDate(String.valueOf(date));
        economyArticleDto.setSubjectCountry(subjectCountry.name());
        economyArticleDto.setImportance(importance.name());
        economyArticleDto.setSummary(summary);
        economyArticleDto.setPressNumber(pressNumber);
        try {
            economyArticleDto.setMappedEconomyContentNumbers(new ObjectMapper().writeValueAsString(new HashMap<>(){{
                put(MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE, mappedEconomyContentNumbers);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + mappedEconomyContentNumbers);
        }
        return economyArticleDto;
    }

    public static final class EconomyArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private List<Long> mappedEconomyContentNumbers;

        public EconomyArticleBuilder article(final EconomyArticle economyArticle) {
            this.number = economyArticle.getNumber();
            this.name = economyArticle.getName();
            this.link = economyArticle.getLink();
            this.date = economyArticle.getDate();
            this.subjectCountry = economyArticle.getSubjectCountry();
            this.importance = economyArticle.getImportance();
            this.summary = economyArticle.getSummary();
            this.pressNumber = economyArticle.getPressNumber();
            this.mappedEconomyContentNumbers = economyArticle.getMappedEconomyContentNumbers();
            return this;
        }

        public EconomyArticleBuilder articleDto(final EconomyArticleDto economyArticleDto) {
            this.number = economyArticleDto.getNumber();
            this.name = economyArticleDto.getName();
            this.link = economyArticleDto.getLink();
            this.date = convertFromStringToLocalDate(economyArticleDto.getDate());
            this.subjectCountry = Country.valueOf(economyArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(economyArticleDto.getImportance());
            this.summary = economyArticleDto.getSummary();
            this.pressNumber = economyArticleDto.getPressNumber();
            try {
                Map<String, List<Long>> mappedEconomyContentNumbersMap = new ObjectMapper()
                        .readValue(economyArticleDto.getMappedEconomyContentNumbers(), new TypeReference<>() {
                });
                this.mappedEconomyContentNumbers = mappedEconomyContentNumbersMap.get(MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + economyArticleDto.getMappedEconomyContentNumbers());
            }
            return this;
        }

        public EconomyArticleBuilder articleDtoNoNumber(final EconomyArticleDtoNoNumber economyArticleDto) {
            this.name = economyArticleDto.getName();
            this.link = economyArticleDto.getLink();
            this.date = convertFromStringToLocalDate(economyArticleDto.getDate());
            this.subjectCountry = Country.valueOf(economyArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(economyArticleDto.getImportance());
            this.summary = economyArticleDto.getSummary();
            this.pressNumber = economyArticleDto.getPressNumber();
            try {
                Map<String, List<Long>> mappedEconomyContentNumbersMap = new ObjectMapper()
                        .readValue(economyArticleDto.getMappedEconomyContentNumbers(), new TypeReference<>() {
                });
                this.mappedEconomyContentNumbers = mappedEconomyContentNumbersMap.get(MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + economyArticleDto.getMappedEconomyContentNumbers());
            }
            return this;
        }
    }
}
