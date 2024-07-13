package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {
    private AtomicReference<String> number;
    private AtomicReference<String> name;
    private AtomicReference<LocalDate> date;
    private AtomicReference<String> link;

    private Article(String number, String name, LocalDate date, String link) {
        this.number = new AtomicReference<>(number);
        this.name = new AtomicReference<>(name);
        this.date = new AtomicReference<>(date);
        this.link = new AtomicReference<>(link);
    }

    public String getNumber() {
        return number.get();
    }

    public String getName() {
        return name.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public String getLink() {
        return link.get();
    }

    public static class ArticleBuilder {
        private String number;
        private String name;
        private LocalDate date;
        private String link;

        public Article.ArticleBuilder article(Article article) {
            number = article.getNumber();
            name = article.getName();
            date = article.getDate();
            link = article.getLink();
            return this;
        }

        public Article.ArticleBuilder number(String number) {
            this.number = number;
            return this;
        }

        public Article.ArticleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Article.ArticleBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Article.ArticleBuilder link(String link) {
            this.link = link;
            return this;
        }

        public Article build() {
            return new Article(number, name, date, link);
        }
    }
}
