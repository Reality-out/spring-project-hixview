package site.hixview.aggregate.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;
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
import static site.hixview.aggregate.vo.WordSnake.MAPPED_COMPANY_CODES_SNAKE;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class CompanyArticle implements ConvertibleToWholeDto<CompanyArticleDto, CompanyArticleDtoNoNumber> {

    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Country subjectCountry;
    private final Importance importance;
    private final String summary;
    private final Long pressNumber;
    private final List<String> mappedCompanyCodes;

    @Override
    public CompanyArticleDto toDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(number);
        companyArticleDto.setName(name);
        companyArticleDto.setLink(link);
        companyArticleDto.setDate(String.valueOf(date));
        companyArticleDto.setSubjectCountry(subjectCountry.name());
        companyArticleDto.setImportance(importance.name());
        companyArticleDto.setSummary(summary);
        companyArticleDto.setPressNumber(pressNumber);
        try {
            companyArticleDto.setMappedCompanyCodes(new ObjectMapper().writeValueAsString(new HashMap<>(){{
                put(MAPPED_COMPANY_CODES_SNAKE, mappedCompanyCodes);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + mappedCompanyCodes);
        }
        return companyArticleDto;
    }
    
    @Override
    public CompanyArticleDtoNoNumber toDtoNoNumber() {
        CompanyArticleDtoNoNumber companyArticleDto = new CompanyArticleDtoNoNumber();
        companyArticleDto.setName(name);
        companyArticleDto.setLink(link);
        companyArticleDto.setDate(String.valueOf(date));
        companyArticleDto.setSubjectCountry(subjectCountry.name());
        companyArticleDto.setImportance(importance.name());
        companyArticleDto.setSummary(summary);
        companyArticleDto.setPressNumber(pressNumber);
        try {
            companyArticleDto.setMappedCompanyCodes(new ObjectMapper().writeValueAsString(new HashMap<>(){{
                put(MAPPED_COMPANY_CODES_SNAKE, mappedCompanyCodes);
            }}));
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException(CANNOT_MAP_TO_JSON + mappedCompanyCodes);
        }
        return companyArticleDto;
    }

    public static final class CompanyArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private List<String> mappedCompanyCodes;

        public CompanyArticleBuilder article(final CompanyArticle companyArticle) {
            this.number = companyArticle.getNumber();
            this.name = companyArticle.getName();
            this.link = companyArticle.getLink();
            this.date = companyArticle.getDate();
            this.subjectCountry = companyArticle.getSubjectCountry();
            this.importance = companyArticle.getImportance();
            this.summary = companyArticle.getSummary();
            this.pressNumber = companyArticle.getPressNumber();
            this.mappedCompanyCodes = companyArticle.getMappedCompanyCodes();
            return this;
        }

        public CompanyArticleBuilder articleDto(final CompanyArticleDto companyArticleDto) {
            this.number = companyArticleDto.getNumber();
            this.name = companyArticleDto.getName();
            this.link = companyArticleDto.getLink();
            this.date = convertFromStringToLocalDate(companyArticleDto.getDate());
            this.subjectCountry = Country.valueOf(companyArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(companyArticleDto.getImportance());
            this.summary = companyArticleDto.getSummary();
            this.pressNumber = companyArticleDto.getPressNumber();
            try {
                Map<String, List<String>> mappedCompanyCodesMap = new ObjectMapper().readValue(companyArticleDto.getMappedCompanyCodes(), new TypeReference<>() {
                });
                this.mappedCompanyCodes = mappedCompanyCodesMap.get(MAPPED_COMPANY_CODES_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + companyArticleDto.getMappedCompanyCodes());
            }
            return this;
        }

        public CompanyArticleBuilder articleDtoNoNumber(final CompanyArticleDtoNoNumber companyArticleDto) {
            this.name = companyArticleDto.getName();
            this.link = companyArticleDto.getLink();
            this.date = convertFromStringToLocalDate(companyArticleDto.getDate());
            this.subjectCountry = Country.valueOf(companyArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(companyArticleDto.getImportance());
            this.summary = companyArticleDto.getSummary();
            this.pressNumber = companyArticleDto.getPressNumber();
            try {
                Map<String, List<String>> mappedCompanyCodesMap = new ObjectMapper()
                        .readValue(companyArticleDto.getMappedCompanyCodes(), new TypeReference<>() {
                        });
                this.mappedCompanyCodes = mappedCompanyCodesMap.get(MAPPED_COMPANY_CODES_SNAKE);
            } catch (JsonProcessingException e) {
                throw new RuntimeJsonParsingException(CANNOT_PARSE_TO_LIST + companyArticleDto.getMappedCompanyCodes());
            }
            return this;
        }
    }
}
