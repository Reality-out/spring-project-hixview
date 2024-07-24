package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyArticle {
    private AtomicLong number;
    private AtomicReference<String> name;
    private AtomicReference<String> press;
    private AtomicReference<String> subjectCompany;
    private AtomicReference<String> link;
    private AtomicReference<LocalDate> date;
    private AtomicInteger importance;

    private CompanyArticle(Long number, String name, String press,
                           String subjectCompany, String link, LocalDate date, int importance) {
        this.number = new AtomicLong(number);
        this.name = new AtomicReference<>(name);
        this.press = new AtomicReference<>(press);
        this.subjectCompany = new AtomicReference<>(subjectCompany);
        this.link = new AtomicReference<>(link);
        this.date = new AtomicReference<>(date);
        this.importance = new AtomicInteger(importance);
    }

    public Long getNumber() {
        return number.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPress() {
        return press.get();
    }

    public String getSubjectCompany() {
        return subjectCompany.get();
    }

    public String getLink() {
        return link.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public int getImportance() {
        return importance.get();
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>() {{
            put("number", number.get());
            put("name", name.get());
            put("press", press.get());
            put("subjectCompany", subjectCompany.get());
            put("link", link.get());
            put("date", date.get());
            put("importance", importance.get());
        }};
    }

    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put("name", name.get());
            put("press", press.get());
            put("subjectCompany", subjectCompany.get());
            put("link", link.get());
            put("date", date.get());
            put("importance", importance.get());
        }};
    }

    public static class ArticleBuilder {
        private Long number = -1L;
        private String name;
        private String press;
        private String subjectCompany;
        private String link;
        private LocalDate date;
        private int importance;

        public CompanyArticle.ArticleBuilder article(CompanyArticle article) {
            number = article.getNumber();
            name = article.getName();
            press = article.getPress();
            subjectCompany = article.getSubjectCompany();
            link = article.getLink();
            date = article.getDate();
            importance = article.getImportance();
            return this;
        }

        public CompanyArticle.ArticleBuilder number(Long number) {
            this.number = number;
            return this;
        }

        public CompanyArticle.ArticleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompanyArticle.ArticleBuilder press(String press) {
            this.press = press;
            return this;
        }

        public CompanyArticle.ArticleBuilder subjectCompany(String subjectCompany) {
            this.subjectCompany = subjectCompany;
            return this;
        }

        public CompanyArticle.ArticleBuilder link(String link) {
            this.link = link;
            return this;
        }

        public CompanyArticle.ArticleBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public CompanyArticle.ArticleBuilder importance(int importance) {
            this.importance = importance;
            return this;
        }

        public CompanyArticle build() {
            return new CompanyArticle(number, name, press, subjectCompany, link, date, importance);
        }
    }
}