package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.ArticleDto;
import site.hixview.aggregate.dto.ArticleDtoNoNumber;
import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class Article implements ConvertibleToWholeDto<ArticleDto, ArticleDtoNoNumber> {

    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Classification classification;
    private final Country subjectCountry;
    private final Importance importance;
    private final String summary;
    private final Long pressNumber;

    @Override
    public ArticleDto toDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setNumber(number);
        articleDto.setName(name);
        articleDto.setLink(link);
        articleDto.setDate(String.valueOf(date));
        articleDto.setClassification(classification.name());
        articleDto.setSubjectCountry(subjectCountry.name());
        articleDto.setImportance(importance.name());
        articleDto.setSummary(summary);
        articleDto.setPressNumber(pressNumber);
        return articleDto;
    }

    @Override
    public ArticleDtoNoNumber toDtoNoNumber() {
        ArticleDtoNoNumber articleDtoNoNumber = new ArticleDtoNoNumber();
        articleDtoNoNumber.setName(name);
        articleDtoNoNumber.setLink(link);
        articleDtoNoNumber.setDate(String.valueOf(date));
        articleDtoNoNumber.setClassification(classification.name());
        articleDtoNoNumber.setSubjectCountry(subjectCountry.name());
        articleDtoNoNumber.setImportance(importance.name());
        articleDtoNoNumber.setSummary(summary);
        articleDtoNoNumber.setPressNumber(pressNumber);
        return articleDtoNoNumber;
    }

    public static final class ArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Classification classification;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;

        public ArticleBuilder article(final Article article) {
            this.number = article.getNumber();
            this.name = article.getName();
            this.link = article.getLink();
            this.date = article.getDate();
            this.classification = article.getClassification();
            this.subjectCountry = article.getSubjectCountry();
            this.importance = article.getImportance();
            this.summary = article.getSummary();
            this.pressNumber = article.getPressNumber();
            return this;
        }

        public ArticleBuilder articleDto(final ArticleDto articleDto) {
            this.number = articleDto.getNumber();
            this.name = articleDto.getName();
            this.link = articleDto.getLink();
            this.date = convertFromStringToLocalDate(articleDto.getDate());
            this.classification = Classification.valueOf(articleDto.getClassification());
            this.subjectCountry = Country.valueOf(articleDto.getSubjectCountry());
            this.importance = Importance.valueOf(articleDto.getImportance());
            this.summary = articleDto.getSummary();
            this.pressNumber = articleDto.getPressNumber();
            return this;
        }

        public ArticleBuilder articleDtoNoNumber(final ArticleDtoNoNumber articleDtoNoNumber) {
            this.name = articleDtoNoNumber.getName();
            this.link = articleDtoNoNumber.getLink();
            this.date = convertFromStringToLocalDate(articleDtoNoNumber.getDate());
            this.classification = Classification.valueOf(articleDtoNoNumber.getClassification());
            this.subjectCountry = Country.valueOf(articleDtoNoNumber.getSubjectCountry());
            this.importance = Importance.valueOf(articleDtoNoNumber.getImportance());
            this.summary = articleDtoNoNumber.getSummary();
            this.pressNumber = articleDtoNoNumber.getPressNumber();
            return this;
        }
    }
}
