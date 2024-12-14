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
public class CompanyArticle {
    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Country subjectCountry;
    private final Importance importance;
    private final String summary;
    private final Long pressNumber;
    private final List<String> mappedCompanyCodes;

    public static class CompanyArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private List<String> mappedCompanyCodes;

        public CompanyArticleBuilder companyArticle(final CompanyArticle companyArticle) {
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
        
        public CompanyArticle build() {
            return new CompanyArticle(this.number, this.name, this.link, this.date, this.subjectCountry, this.importance, this.summary, this.pressNumber, this.mappedCompanyCodes);
        }
    }
}
