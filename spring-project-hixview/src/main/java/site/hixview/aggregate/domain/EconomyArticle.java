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
public class EconomyArticle {
    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Country subjectCountry;
    private final Importance importance;
    private final String summary;
    private final Long pressNumber;
    private final List<Long> mappedEconomyContentNumbers;

    public static class EconomyArticleBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Country subjectCountry;
        private Importance importance;
        private String summary;
        private Long pressNumber;
        private List<Long> mappedEconomyContentNumbers;

        public EconomyArticleBuilder economyArticle(final EconomyArticle economyArticle) {
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
        
        public EconomyArticle build() {
            return new EconomyArticle(this.number, this.name, this.link, this.date, this.subjectCountry, this.importance, this.summary, this.pressNumber, this.mappedEconomyContentNumbers);
        }
    }
}