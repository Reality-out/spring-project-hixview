package site.hixview.aggregate.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.aggregate.error.RuntimeJsonParsingException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.*;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class IndustryArticle implements ConvertibleToDto<IndustryArticleDto> {

    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Country subjectCountry;
    private final Importance importance;
    private final String summary;
    private final Long pressNumber;
    private final Long firstCategoryNumber;
    private final List<Long> secondCategoryNumbers;

    @Override
    public IndustryArticleDto toDto() {
        IndustryArticleDto industryArticleDto = new IndustryArticleDto();
        industryArticleDto.setNumber(number);
        industryArticleDto.setName(name);
        industryArticleDto.setLink(link);
        industryArticleDto.setDate(String.valueOf(date));
        industryArticleDto.setSubjectCountry(subjectCountry.name());
        industryArticleDto.setImportance(importance.name());
        industryArticleDto.setSummary(summary);
        industryArticleDto.setPressNumber(pressNumber);
        industryArticleDto.setFirstCategoryNumber(firstCategoryNumber);
        try {
            industryArticleDto.setMappedSecondCategoryNumbers(new ObjectMapper().writeValueAsString(new HashMap<>() {{
                put(MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE, secondCategoryNumbers);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + secondCategoryNumbers);
        }
        return industryArticleDto;
    }

    public static final class IndustryArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Classification classification;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private Long firstCategoryNumber;
        private List<Long> secondCategoryNumbers;
        
        public IndustryArticleBuilder article(final IndustryArticle industryArticle) {
            this.number = industryArticle.getNumber();
            this.name = industryArticle.getName();
            this.link = industryArticle.getLink();
            this.date = industryArticle.getDate();
            this.subjectCountry = industryArticle.getSubjectCountry();
            this.importance = industryArticle.getImportance();
            this.summary = industryArticle.getSummary();
            this.pressNumber = industryArticle.getPressNumber();
            this.firstCategoryNumber = industryArticle.getFirstCategoryNumber();
            this.secondCategoryNumbers = industryArticle.getSecondCategoryNumbers();
            return this;
        }

        public IndustryArticleBuilder articleDto(final IndustryArticleDto industryArticleDto) {
            this.number = industryArticleDto.getNumber();
            this.name = industryArticleDto.getName();
            this.link = industryArticleDto.getLink();
            this.date = convertFromStringToLocalDate(industryArticleDto.getDate());
            this.subjectCountry = Country.valueOf(industryArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(industryArticleDto.getImportance());
            this.summary = industryArticleDto.getSummary();
            this.pressNumber = industryArticleDto.getPressNumber();
            this.firstCategoryNumber = industryArticleDto.getFirstCategoryNumber();
            try {
                Map<String, List<Long>> secondCategoryNumbersMap = new ObjectMapper()
                        .readValue(industryArticleDto.getMappedSecondCategoryNumbers(), new TypeReference<>() {
                });
                this.secondCategoryNumbers = secondCategoryNumbersMap.get(MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + industryArticleDto.getMappedSecondCategoryNumbers());
            }
            return this;
        }

        public IndustryArticleBuilder articleDtoNoNumber(final IndustryArticleDtoNoNumber industryArticleDto) {
            this.name = industryArticleDto.getName();
            this.link = industryArticleDto.getLink();
            this.date = convertFromStringToLocalDate(industryArticleDto.getDate());
            this.subjectCountry = Country.valueOf(industryArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(industryArticleDto.getImportance());
            this.summary = industryArticleDto.getSummary();
            this.pressNumber = industryArticleDto.getPressNumber();
            this.firstCategoryNumber = industryArticleDto.getFirstCategoryNumber();
            try {
                Map<String, List<Long>> secondCategoryNumbersMap = new ObjectMapper()
                        .readValue(industryArticleDto.getMappedSecondCategoryNumbers(), new TypeReference<>() {
                        });
                this.secondCategoryNumbers = secondCategoryNumbersMap.get(MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + industryArticleDto.getMappedSecondCategoryNumbers());
            }            return this;
        }
    }
}
