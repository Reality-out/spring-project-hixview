package site.hixview.domain.entity.article;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Press;
import site.hixview.domain.entity.SecondCategory;

import java.time.LocalDate;
import java.util.HashMap;

import static site.hixview.domain.vo.name.EntityName.Article.*;

@Getter
@Builder(access = AccessLevel.PUBLIC)
public class IndustryArticle extends Article {

    @NotNull
    private final FirstCategory subjectFirstCategory;

    @NotNull
    private final SecondCategory subjectSecondCategory;

    public IndustryArticleDto toDto() {
        IndustryArticleDto IndustryArticleDto = new IndustryArticleDto();
        IndustryArticleDto.setName(name);
        IndustryArticleDto.setPress(press.name());
        IndustryArticleDto.setLink(link);
        IndustryArticleDto.setYear(date.getYear());
        IndustryArticleDto.setMonth(date.getMonthValue());
        IndustryArticleDto.setDays(date.getDayOfMonth());
        IndustryArticleDto.setImportance(importance);
        IndustryArticleDto.setSubjectFirstCategory(subjectFirstCategory.name());
        IndustryArticleDto.setSubjectSecondCategory(subjectSecondCategory.name());
        return IndustryArticleDto;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put(NUMBER, number);
            putAll(toMapWithNoNumber());
        }};
    }

    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            putAll(IndustryArticle.super.toMapWithNoNumber());
            put(SUBJECT_FIRST_CATEGORY, subjectFirstCategory);
            put(SUBJECT_SECOND_CATEGORY, subjectSecondCategory);
        }};
    }

    private IndustryArticle(Long number, String name, Press press, String link, LocalDate date,
                            Integer importance, FirstCategory subjectFirstCategory, SecondCategory subjectSecondCategory) {
        super(number, name, press, link, date, importance);
        this.subjectFirstCategory = subjectFirstCategory;
        this.subjectSecondCategory = subjectSecondCategory;
    }

    public static class IndustryArticleBuilder extends ArticleBuilder {
        private FirstCategory subjectFirstCategory;
        private SecondCategory subjectSecondCategory;

        public IndustryArticleBuilder() {}

        public IndustryArticleBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public IndustryArticleBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public IndustryArticleBuilder press(final Press press) {
            this.press = press;
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

        public IndustryArticleBuilder importance(final Integer importance) {
            this.importance = importance;
            return this;
        }

        public IndustryArticleBuilder article(IndustryArticle article) {
            number = article.getNumber();
            name = article.getName();
            press = article.getPress();
            link = article.getLink();
            date = article.getDate();
            importance = article.getImportance();
            subjectFirstCategory = article.getSubjectFirstCategory();
            subjectSecondCategory = article.getSubjectSecondCategory();
            return this;
        }

        public IndustryArticleBuilder articleDto(IndustryArticleDto articleDto) {
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            importance = articleDto.getImportance();
            subjectFirstCategory = FirstCategory.valueOf(articleDto.getSubjectFirstCategory());
            subjectSecondCategory = SecondCategory.valueOf(articleDto.getSubjectSecondCategory());
            return this;
        }

        public IndustryArticle build() {
            return new IndustryArticle(this.number, this.name, this.press, this.link,
                    this.date, this.importance, this.subjectFirstCategory, this.subjectSecondCategory);
        }
    }
}
