package site.hixview.aggregate.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;
import java.util.List;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_JSON;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_PARSE_TO_LIST;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class EconomyArticle implements ConvertibleToDto<EconomyArticleDto> {

    private final Article article;
    private final List<Long> economyContentNumbers;

    @Override
    public EconomyArticleDto toDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setNumber(article.getNumber());
        economyArticleDto.setName(article.getName());
        economyArticleDto.setLink(article.getLink());
        economyArticleDto.setDate(String.valueOf(article.getDate()));
        economyArticleDto.setClassification(article.getClassification().name());
        economyArticleDto.setSubjectCountry(article.getSubjectCountry().name());
        economyArticleDto.setImportance(article.getImportance().name());
        economyArticleDto.setSummary(article.getSummary());
        economyArticleDto.setPressNumber(article.getPressNumber());
        try {
            economyArticleDto.setEconomyContentNumbers(new ObjectMapper().writeValueAsString(economyContentNumbers));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(CANNOT_PARSE_TO_JSON + economyContentNumbers);
        }
        return economyArticleDto;
    }

    private EconomyArticle(final Long number, final String name, final String link, final LocalDate date, final Classification classification, final Country subjectCountry, final Importance importance, final String summary, final Long pressNumber, final List<Long> economyContentNumbers) {
        article = Article.builder().number(number).name(name).link(link).date(date).classification(classification).subjectCountry(subjectCountry).importance(importance).summary(summary).pressNumber(pressNumber).build();
        this.economyContentNumbers = economyContentNumbers;
    }

    public static final class EconomyArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Classification classification;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private List<Long> economyContentNumbers;

        public EconomyArticleBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public EconomyArticleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public EconomyArticleBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public EconomyArticleBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public EconomyArticleBuilder classification(final Classification classification) {
            this.classification = classification;
            return this;
        }

        public EconomyArticleBuilder subjectCountry(final Country subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public EconomyArticleBuilder importance(final Importance importance) {
            this.importance = importance;
            return this;
        }

        public EconomyArticleBuilder summary(final String summary) {
            this.summary = summary;
            return this;
        }

        public EconomyArticleBuilder pressNumber(final Long pressNumber) {
            this.pressNumber = pressNumber;
            return this;
        }

        public EconomyArticleBuilder article(final EconomyArticle economyArticle) {
            this.number = economyArticle.getArticle().getNumber();
            this.name = economyArticle.getArticle().getName();
            this.link = economyArticle.getArticle().getLink();
            this.date = economyArticle.getArticle().getDate();
            this.classification = economyArticle.getArticle().getClassification();
            this.subjectCountry = economyArticle.getArticle().getSubjectCountry();
            this.importance = economyArticle.getArticle().getImportance();
            this.summary = economyArticle.getArticle().getSummary();
            this.pressNumber = economyArticle.getArticle().getPressNumber();
            this.economyContentNumbers = economyArticle.getEconomyContentNumbers();
            return this;
        }

        public EconomyArticleBuilder articleDto(final EconomyArticleDto economyArticleDto) {
            this.number = economyArticleDto.getNumber();
            this.name = economyArticleDto.getName();
            this.link = economyArticleDto.getLink();
            this.date = convertFromStringToLocalDate(economyArticleDto.getDate());
            this.classification = Classification.valueOf(economyArticleDto.getClassification());
            this.subjectCountry = Country.valueOf(economyArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(economyArticleDto.getImportance());
            this.summary = economyArticleDto.getSummary();
            this.pressNumber = economyArticleDto.getPressNumber();
            try {
                this.economyContentNumbers = new ObjectMapper().readValue(economyArticleDto.getEconomyContentNumbers(), new TypeReference<>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(CANNOT_PARSE_TO_LIST + economyArticleDto.getEconomyContentNumbers());
            }
            return this;
        }

        public EconomyArticleBuilder articleDtoNoNumber(final EconomyArticleDtoNoNumber economyArticleDtoNoNumber) {
            this.name = economyArticleDtoNoNumber.getName();
            this.link = economyArticleDtoNoNumber.getLink();
            this.date = convertFromStringToLocalDate(economyArticleDtoNoNumber.getDate());
            this.classification = Classification.valueOf(economyArticleDtoNoNumber.getClassification());
            this.subjectCountry = Country.valueOf(economyArticleDtoNoNumber.getSubjectCountry());
            this.importance = Importance.valueOf(economyArticleDtoNoNumber.getImportance());
            this.summary = economyArticleDtoNoNumber.getSummary();
            this.pressNumber = economyArticleDtoNoNumber.getPressNumber();
            try {
                this.economyContentNumbers = new ObjectMapper().readValue(economyArticleDtoNoNumber.getEconomyContentNumbers(), new TypeReference<>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(CANNOT_PARSE_TO_LIST + economyArticleDtoNoNumber.getEconomyContentNumbers());
            }
            return this;
        }

        public EconomyArticle build() {
            return new EconomyArticle(this.number, this.name, this.link, this.date, this.classification, this.subjectCountry, this.importance, this.summary, this.pressNumber, this.economyContentNumbers);
        }
    }
}
