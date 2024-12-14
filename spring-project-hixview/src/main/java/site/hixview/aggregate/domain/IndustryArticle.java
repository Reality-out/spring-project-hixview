package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class IndustryArticle {
    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Country subjectCountry;
    private final Importance importance;
    private final String summary;
    private final Long pressNumber;
    private final Long firstCategoryNumber;
    private final List<Long> mappedSecondCategoryNumbers;

    public static class IndustryArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private List<Long> mappedSecondCategoryNumbers;

        public IndustryArticle.IndustryArticleBuilder industryArticle(final IndustryArticle industryArticle) {
            this.number = industryArticle.getNumber();
            this.name = industryArticle.getName();
            this.link = industryArticle.getLink();
            this.date = industryArticle.getDate();
            this.subjectCountry = industryArticle.getSubjectCountry();
            this.importance = industryArticle.getImportance();
            this.summary = industryArticle.getSummary();
            this.pressNumber = industryArticle.getPressNumber();
            this.mappedSecondCategoryNumbers = industryArticle.getMappedSecondCategoryNumbers();
            return this;
        }

        public IndustryArticle build() {
            return new IndustryArticle(this.number, this.name, this.link, this.date, this.subjectCountry, this.importance, this.summary, this.pressNumber, this.firstCategoryNumber, this.mappedSecondCategoryNumbers);
        }
    }
}
