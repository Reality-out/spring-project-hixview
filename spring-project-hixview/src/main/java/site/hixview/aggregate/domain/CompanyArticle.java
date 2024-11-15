package site.hixview.aggregate.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;
import java.util.List;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_JSON;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;

@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class CompanyArticle implements ConvertibleToDto<CompanyArticleDto> {

    private final Article article;
    private final List<String> companyCodes;

    @Override
    public CompanyArticleDto toDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(article.getNumber());
        companyArticleDto.setName(article.getName());
        companyArticleDto.setLink(article.getLink());
        companyArticleDto.setDate(String.valueOf(article.getDate()));
        companyArticleDto.setClassification(article.getClassification().name());
        companyArticleDto.setSubjectCountry(article.getSubjectCountry().name());
        companyArticleDto.setImportance(article.getImportance().name());
        companyArticleDto.setSummary(article.getSummary());
        companyArticleDto.setPressNumber(article.getPressNumber());
        try {
            companyArticleDto.setCompanyCodes(new ObjectMapper().writeValueAsString(companyCodes));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(CANNOT_PARSE_TO_JSON + companyCodes);
        }
        return companyArticleDto;
    }

    private CompanyArticle(final Long number, final String name, final String link, final LocalDate date, final Classification classification, final Country subjectCountry, final Importance importance, final String summary, final Long pressNumber, final List<String> companyCodes) {
        article = Article.builder().number(number).name(name).link(link).date(date).classification(classification).subjectCountry(subjectCountry).importance(importance).summary(summary).pressNumber(pressNumber).build();
        this.companyCodes = companyCodes;
    }

    public static final class CompanyArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Classification classification;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private List<String> companyCodes;

        public CompanyArticleBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public CompanyArticleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public CompanyArticleBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public CompanyArticleBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public CompanyArticleBuilder classification(final Classification classification) {
            this.classification = classification;
            return this;
        }

        public CompanyArticleBuilder subjectCountry(final Country subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public CompanyArticleBuilder importance(final Importance importance) {
            this.importance = importance;
            return this;
        }

        public CompanyArticleBuilder summary(final String summary) {
            this.summary = summary;
            return this;
        }

        public CompanyArticleBuilder pressNumber(final Long pressNumber) {
            this.pressNumber = pressNumber;
            return this;
        }

        public CompanyArticleBuilder article(final CompanyArticle companyArticle) {
            this.number = companyArticle.getArticle().getNumber();
            this.name = companyArticle.getArticle().getName();
            this.link = companyArticle.getArticle().getLink();
            this.date = companyArticle.getArticle().getDate();
            this.classification = companyArticle.getArticle().getClassification();
            this.subjectCountry = companyArticle.getArticle().getSubjectCountry();
            this.importance = companyArticle.getArticle().getImportance();
            this.summary = companyArticle.getArticle().getSummary();
            this.pressNumber = companyArticle.getArticle().getPressNumber();
            this.companyCodes = companyArticle.getCompanyCodes();
            return this;
        }

        public CompanyArticleBuilder articleDto(final CompanyArticleDto companyArticleDto) {
            this.number = companyArticleDto.getNumber();
            this.name = companyArticleDto.getName();
            this.link = companyArticleDto.getLink();
            this.date = convertFromStringToLocalDate(companyArticleDto.getDate());
            this.classification = Classification.valueOf(companyArticleDto.getClassification());
            this.subjectCountry = Country.valueOf(companyArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(companyArticleDto.getImportance());
            this.summary = companyArticleDto.getSummary();
            this.pressNumber = companyArticleDto.getPressNumber();
            try {
                this.companyCodes = new ObjectMapper().readValue(companyArticleDto.getCompanyCodes(), new TypeReference<>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(CANNOT_PARSE_TO_LIST + companyArticleDto.getCompanyCodes());
            }
            return this;
        }

        public CompanyArticleBuilder articleDtoNoNumber(final CompanyArticleDtoNoNumber companyArticleDtoNoNumber) {
            this.name = companyArticleDtoNoNumber.getName();
            this.link = companyArticleDtoNoNumber.getLink();
            this.date = convertFromStringToLocalDate(companyArticleDtoNoNumber.getDate());
            this.classification = Classification.valueOf(companyArticleDtoNoNumber.getClassification());
            this.subjectCountry = Country.valueOf(companyArticleDtoNoNumber.getSubjectCountry());
            this.importance = Importance.valueOf(companyArticleDtoNoNumber.getImportance());
            this.summary = companyArticleDtoNoNumber.getSummary();
            this.pressNumber = companyArticleDtoNoNumber.getPressNumber();
            try {
                this.companyCodes = new ObjectMapper().readValue(companyArticleDtoNoNumber.getCompanyCodes(), new TypeReference<>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(CANNOT_PARSE_TO_LIST + companyArticleDtoNoNumber.getCompanyCodes());
            }
            return this;
        }

        public CompanyArticle build() {
            return new CompanyArticle(this.number, this.name, this.link, this.date, this.classification, this.subjectCountry, this.importance, this.summary, this.pressNumber, this.companyCodes);
        }
    }
}
