package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;

import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REGEX.URL_REGEX;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyArticle {

    private final Long number;

    @NotBlank
    private final String name;

    @NotNull
    private final Press press;

    @NotBlank
    private final String subjectCompany;

    @NotBlank
    @Pattern(regexp = URL_REGEX)
    private final String link;

    @NotNull
    private final LocalDate date;

    @NotNull
    private final Integer importance;

    public CompanyArticleDto toDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setName(name);
        companyArticleDto.setPress(press.name());
        companyArticleDto.setSubjectCompany(subjectCompany);
        companyArticleDto.setLink(link);
        companyArticleDto.setYear(date.getYear());
        companyArticleDto.setMonth(date.getMonthValue());
        companyArticleDto.setDays(date.getDayOfMonth());
        companyArticleDto.setImportance(importance);
        return companyArticleDto;
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
            put(SUBJECT_COMPANY, subjectCompany);
            put(LINK, link);
            put(DATE, date);
            put(IMPORTANCE, importance);
        }};
    }

    public static class CompanyArticleBuilder {
        public CompanyArticleBuilder() {}

        public CompanyArticleBuilder article(CompanyArticle article) {
            number = article.getNumber();
            name = article.getName();
            press = article.getPress();
            subjectCompany = article.getSubjectCompany();
            link = article.getLink();
            date = article.getDate();
            importance = article.getImportance();
            return this;
        }

        public CompanyArticleBuilder articleDto(CompanyArticleDto articleDto) {
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            subjectCompany = articleDto.getSubjectCompany();
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDays());
            importance = articleDto.getImportance();
            return this;
        }
    }
}
