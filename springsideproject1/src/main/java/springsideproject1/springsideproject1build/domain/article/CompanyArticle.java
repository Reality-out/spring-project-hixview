package springsideproject1.springsideproject1build.domain.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.HashMap;

import static springsideproject1.springsideproject1build.utility.WordUtils.DATE;
import static springsideproject1.springsideproject1build.utility.WordUtils.NAME;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyArticle {

    private final Long number;

    @NotBlank
    private final String name;

    @NotBlank
    private final Press press;

    @NotBlank
    private final String subjectCompany;

    @NotBlank
    @URL
    private final String link;

    @NotNull
    private final LocalDate date;

    @NotNull
    private final Integer importance;

    public CompanyArticleDto toDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(number);
        companyArticleDto.setName(name);
        companyArticleDto.setPress(press.name());
        companyArticleDto.setSubjectCompany(subjectCompany);
        companyArticleDto.setLink(link);
        companyArticleDto.setYear(date.getYear());
        companyArticleDto.setMonth(date.getMonthValue());
        companyArticleDto.setDate(date.getDayOfMonth());
        companyArticleDto.setImportance(importance);
        return companyArticleDto;
    }

    public CompanyArticleDtoNoNumber toDtoNoNumber() {
        CompanyArticleDtoNoNumber companyArticleDto = new CompanyArticleDtoNoNumber();
        companyArticleDto.setName(name);
        companyArticleDto.setPress(press.name());
        companyArticleDto.setSubjectCompany(subjectCompany);
        companyArticleDto.setLink(link);
        companyArticleDto.setYear(date.getYear());
        companyArticleDto.setMonth(date.getMonthValue());
        companyArticleDto.setDate(date.getDayOfMonth());
        companyArticleDto.setImportance(importance);
        return companyArticleDto;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put("number", number);
            putAll(toMapWithNoNumber());
        }};
    }

    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put(NAME, name);
            put("press", press);
            put("subjectCompany", subjectCompany);
            put("link", link);
            put(DATE, date);
            put("importance", importance);
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
            number = articleDto.getNumber();
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            subjectCompany = articleDto.getSubjectCompany();
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDate());
            importance = articleDto.getImportance();
            return this;
        }

        public CompanyArticleBuilder articleDtoNoNumber(CompanyArticleDtoNoNumber articleDto) {
            name = articleDto.getName();
            press = Press.valueOf(articleDto.getPress());
            subjectCompany = articleDto.getSubjectCompany();
            link = articleDto.getLink();
            date = LocalDate.of(articleDto.getYear(), articleDto.getMonth(), articleDto.getDate());
            importance = articleDto.getImportance();
            return this;
        }
    }
}
