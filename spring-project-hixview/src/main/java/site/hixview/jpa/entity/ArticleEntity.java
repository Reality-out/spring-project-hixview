package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.PRESS_NUM_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.SUBJECT_COUNTRY_SNAKE;

@Entity
@Table(name = ARTICLE)
@Getter
@NoArgsConstructor
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(unique = true, length = 80, nullable = false)
    private String name;

    @Column(unique = true, length = 400, nullable = false)
    private String link;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 20)
    private String classification;

    @Column(name = SUBJECT_COUNTRY_SNAKE, length = 20, nullable = false)
    private String subjectCountry;

    @Column(nullable = false, length = 20)
    private String importance;

    @Column(nullable = false, length = 36)
    private String summary;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = PRESS_NUM_SNAKE, referencedColumnName = NUM, nullable = false)
    private PressEntity press;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ArticleEntity article = (ArticleEntity) obj;
        return new EqualsBuilder().append(getName(), article.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }

    public static ArticleEntityBuilder builder() {
        return new ArticleEntityBuilder();
    }

    private ArticleEntity(String name, String link, LocalDate date, String classification, String subjectCountry, String importance, String summary, PressEntity press) {
        this.name = name;
        this.link = link;
        this.date = date;
        this.classification = classification;
        this.subjectCountry = subjectCountry;
        this.importance = importance;
        this.summary = summary;
        this.press = press;
    }

    public static final class ArticleEntityBuilder {
        private String name;
        private String link;
        private LocalDate date;
        private String classification;
        private String subjectCountry;
        private String importance;
        private String summary;
        private PressEntity press;

        public ArticleEntityBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public ArticleEntityBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public ArticleEntityBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public ArticleEntityBuilder classification(final String classification) {
            this.classification = classification;
            return this;
        }

        public ArticleEntityBuilder subjectCountry(final String subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public ArticleEntityBuilder importance(final String importance) {
            this.importance = importance;
            return this;
        }

        public ArticleEntityBuilder summary(final String summary) {
            this.summary = summary;
            return this;
        }

        public ArticleEntityBuilder press(final PressEntity press) {
            this.press = press;
            return this;
        }

        public ArticleEntityBuilder article(final ArticleEntity article) {
            this.name = article.getName();
            this.link = article.getLink();
            this.date = article.getDate();
            this.classification = article.getClassification();
            this.subjectCountry = article.getSubjectCountry();
            this.importance = article.getImportance();
            this.summary = article.getSummary();
            this.press = article.getPress();
            return this;
        }

        public ArticleEntity build() {
            return new ArticleEntity(this.name, this.link, this.date, this.classification, this.subjectCountry, this.importance, this.summary, this.press);
        }
    }
}
