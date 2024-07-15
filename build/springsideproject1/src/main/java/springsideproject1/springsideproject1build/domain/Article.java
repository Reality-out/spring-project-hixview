package springsideproject1.springsideproject1build.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {
    private AtomicLong number;
    private AtomicReference<String> name;
    private AtomicReference<LocalDate> date;
    private AtomicReference<String> link;
    private AtomicInteger importance;

    private Article(Long number, String name, LocalDate date, String link, int importance) {
        this.number = new AtomicLong(number);
        this.name = new AtomicReference<>(name);
        this.date = new AtomicReference<>(date);
        this.link = new AtomicReference<>(link);
        this.importance = new AtomicInteger(importance);
    }

    public Long getNumber() {
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

    public int getImportance() {
        return importance.get();
    }

    public static class ArticleBuilder {
        private Long number = -1L;
        private String name;
        private LocalDate date;
        private String link;
        private int importance;

        public Article.ArticleBuilder article(Article article) {
            number = article.getNumber();
            name = article.getName();
            date = article.getDate();
            link = article.getLink();
            importance = article.getImportance();
            return this;
        }

        public Article.ArticleBuilder number(Long number) {
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

        public Article.ArticleBuilder importance(int importance) {
            this.importance = importance;
            return this;
        }

        public Article build() {
            return new Article(number, name, date, link, importance);
        }
    }
}
