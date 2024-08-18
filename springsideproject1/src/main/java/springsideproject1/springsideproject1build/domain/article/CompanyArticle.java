package springsideproject1.springsideproject1build.domain.article;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashMap;

import static springsideproject1.springsideproject1build.utility.ConstantUtils.DATE;
import static springsideproject1.springsideproject1build.utility.ConstantUtils.NAME;

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
    @Pattern(regexp = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]{2,3}$")
    private final String link;

    @NotBlank
    private final LocalDate date;

    @NotBlank
    private final Integer importance;

    public CompanyArticleDto toCompanyArticleDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(number);
        companyArticleDto.setName(name);
        companyArticleDto.setPress(press.name());
        companyArticleDto.setSubjectCompany(subjectCompany);
        companyArticleDto.setLink(link);
        companyArticleDto.setYear(String.valueOf(date.getYear()));
        companyArticleDto.setMonth(String.valueOf(date.getMonthValue()));
        companyArticleDto.setDate(String.valueOf(date.getDayOfMonth()));
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
            date = LocalDate.of(Integer.parseInt(articleDto.getYear()),
                    Integer.parseInt(articleDto.getMonth()), Integer.parseInt(articleDto.getDate()));
            importance = articleDto.getImportance();
            return this;
        }
    }
}
