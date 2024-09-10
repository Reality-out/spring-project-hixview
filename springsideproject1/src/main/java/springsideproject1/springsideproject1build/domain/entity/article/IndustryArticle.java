package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import springsideproject1.springsideproject1build.domain.entity.company.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.company.SecondCategory;
import springsideproject1.springsideproject1build.domain.validation.annotation.Importance;

import java.time.LocalDate;
import java.util.HashMap;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.IMPORTANCE;
import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.URL_REGEX;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IndustryArticle {

    private final Long number;

    @NotBlank
    @Size(max = 80)
    private final String name;

    @NotNull
    private final Press press;

    @NotNull
    private final FirstCategory subjectFirstCategory;

    @NotNull
    private final SecondCategory subjectSecondCategory;

    @NotBlank
    @Size(max = 400)
    @Pattern(regexp = URL_REGEX)
    private final String link;

    @NotNull
    @PastOrPresent
    private final LocalDate date;

    @Importance
    private final Integer importance;

    public IndustryArticleDto toDto() {
        IndustryArticleDto IndustryArticleDto = new IndustryArticleDto();
        IndustryArticleDto.setName(name);
        IndustryArticleDto.setPress(press.name());
        IndustryArticleDto.setSubjectFirstCategory(subjectFirstCategory.name());
        IndustryArticleDto.setSubjectSecondCategory(subjectSecondCategory.name());
        IndustryArticleDto.setLink(link);
        IndustryArticleDto.setYear(date.getYear());
        IndustryArticleDto.setMonth(date.getMonthValue());
        IndustryArticleDto.setDays(date.getDayOfMonth());
        IndustryArticleDto.setImportance(importance);
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
            put(NAME, name);
            put(PRESS, press);
            put(SUBJECT_FIRST_CATEGORY, subjectFirstCategory);
            put(SUBJECT_SECOND_CATEGORY, subjectSecondCategory);
            put(LINK, link);
            put(DATE, date);
            put(IMPORTANCE, importance);
        }};
    }

    public static class IndustryArticleBuilder {
        public IndustryArticleBuilder() {}

        public IndustryArticleBuilder article(IndustryArticle article) {
            number = article.getNumber();
            name = article.getName();
            press = article.getPress();
            subjectFirstCategory = article.getSubjectFirstCategory();
            subjectSecondCategory = article.getSubjectSecondCategory();
            link = article.getLink();
            date = article.getDate();
            importance = article.getImportance();
            return this;
        }

        public IndustryArticleBuilder articleDto(IndustryArticleDto articleDto) {
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            subjectFirstCategory = FirstCategory.valueOf(articleDto.getSubjectFirstCategory());
            subjectSecondCategory = SecondCategory.valueOf(articleDto.getSubjectSecondCategory());
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            importance = articleDto.getImportance();
            return this;
        }
    }
}
