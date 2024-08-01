package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyArticle {
    private final Long number;
    private final String name;
    private final String press;
    private final String subjectCompany;
    private final String link;
    private final LocalDate date;
    private final Integer importance;

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put("number", number);
            putAll(toMapWithNoNumber());
        }};
    }

    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put("name", name);
            put("press", press);
            put("subjectCompany", subjectCompany);
            put("link", link);
            put("date", date);
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
    }
}
