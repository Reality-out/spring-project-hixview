package site.hixview.aggregate.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
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
public class IndustryArticle implements ConvertibleToDto<IndustryArticleDto> {

    private final Article article;
    private final Long firstCategoryNumber;
    private final List<Long> secondCategoryNumbers;

    @Override
    public IndustryArticleDto toDto() {
        IndustryArticleDto industryArticleDto = new IndustryArticleDto();
        industryArticleDto.setNumber(article.getNumber());
        industryArticleDto.setName(article.getName());
        industryArticleDto.setLink(article.getLink());
        industryArticleDto.setDate(String.valueOf(article.getDate()));
        industryArticleDto.setClassification(article.getClassification().name());
        industryArticleDto.setSubjectCountry(article.getSubjectCountry().name());
        industryArticleDto.setImportance(article.getImportance().name());
        industryArticleDto.setSummary(article.getSummary());
        industryArticleDto.setPressNumber(article.getPressNumber());
        industryArticleDto.setFirstCategoryNumber(firstCategoryNumber);
        try {
            industryArticleDto.setSecondCategoryNumbers(new ObjectMapper().writeValueAsString(secondCategoryNumbers));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(CANNOT_PARSE_TO_JSON + secondCategoryNumbers);
        }
        return industryArticleDto;
    }

    private IndustryArticle(final Long number, final String name, final String link, final LocalDate date, final Classification classification, final Country subjectCountry, final Importance importance, final String summary, final Long pressNumber, final Long firstCategoryNumber, final List<Long> secondCategoryNumbers) {
        article = Article.builder().number(number).name(name).link(link).date(date).classification(classification).subjectCountry(subjectCountry).importance(importance).summary(summary).pressNumber(pressNumber).build();
        this.firstCategoryNumber = firstCategoryNumber;
        this.secondCategoryNumbers = secondCategoryNumbers;
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

        public IndustryArticleBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public IndustryArticleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public IndustryArticleBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public IndustryArticleBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public IndustryArticleBuilder classification(final Classification classification) {
            this.classification = classification;
            return this;
        }

        public IndustryArticleBuilder subjectCountry(final Country subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public IndustryArticleBuilder importance(final Importance importance) {
            this.importance = importance;
            return this;
        }

        public IndustryArticleBuilder summary(final String summary) {
            this.summary = summary;
            return this;
        }

        public IndustryArticleBuilder pressNumber(final Long pressNumber) {
            this.pressNumber = pressNumber;
            return this;
        }
        
        public IndustryArticleBuilder article(final IndustryArticle industryArticle) {
            this.number = industryArticle.getArticle().getNumber();
            this.name = industryArticle.getArticle().getName();
            this.link = industryArticle.getArticle().getLink();
            this.date = industryArticle.getArticle().getDate();
            this.classification = industryArticle.getArticle().getClassification();
            this.subjectCountry = industryArticle.getArticle().getSubjectCountry();
            this.importance = industryArticle.getArticle().getImportance();
            this.summary = industryArticle.getArticle().getSummary();
            this.pressNumber = industryArticle.getArticle().getPressNumber();
            this.firstCategoryNumber = industryArticle.getFirstCategoryNumber();
            this.secondCategoryNumbers = industryArticle.getSecondCategoryNumbers();
            return this;
        }

        public IndustryArticleBuilder articleDto(final IndustryArticleDto industryArticleDto) {
            this.number = industryArticleDto.getNumber();
            this.name = industryArticleDto.getName();
            this.link = industryArticleDto.getLink();
            this.date = convertFromStringToLocalDate(industryArticleDto.getDate());
            this.classification = Classification.valueOf(industryArticleDto.getClassification());
            this.subjectCountry = Country.valueOf(industryArticleDto.getSubjectCountry());
            this.importance = Importance.valueOf(industryArticleDto.getImportance());
            this.summary = industryArticleDto.getSummary();
            this.pressNumber = industryArticleDto.getPressNumber();
            this.firstCategoryNumber = industryArticleDto.getFirstCategoryNumber();
            try {
                this.secondCategoryNumbers = new ObjectMapper().readValue(industryArticleDto.getSecondCategoryNumbers(), new TypeReference<>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(CANNOT_PARSE_TO_LIST + industryArticleDto.getSecondCategoryNumbers());
            }
            return this;
        }

        public IndustryArticleBuilder articleDtoNoNumber(final IndustryArticleDtoNoNumber industryArticleDtoNoNumber) {
            this.name = industryArticleDtoNoNumber.getName();
            this.link = industryArticleDtoNoNumber.getLink();
            this.date = convertFromStringToLocalDate(industryArticleDtoNoNumber.getDate());
            this.classification = Classification.valueOf(industryArticleDtoNoNumber.getClassification());
            this.subjectCountry = Country.valueOf(industryArticleDtoNoNumber.getSubjectCountry());
            this.importance = Importance.valueOf(industryArticleDtoNoNumber.getImportance());
            this.summary = industryArticleDtoNoNumber.getSummary();
            this.pressNumber = industryArticleDtoNoNumber.getPressNumber();
            this.firstCategoryNumber = industryArticleDtoNoNumber.getFirstCategoryNumber();
            try {
                this.secondCategoryNumbers = new ObjectMapper().readValue(industryArticleDtoNoNumber.getSecondCategoryNumbers(), new TypeReference<>(){});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(CANNOT_PARSE_TO_LIST + industryArticleDtoNoNumber.getSecondCategoryNumbers());
            }
            return this;
        }

        public IndustryArticle build() {
            return new IndustryArticle(this.number, this.name, this.link, this.date, this.classification, this.subjectCountry, this.importance, this.summary, this.pressNumber, this.firstCategoryNumber, this.secondCategoryNumbers);
        }
    }
}
