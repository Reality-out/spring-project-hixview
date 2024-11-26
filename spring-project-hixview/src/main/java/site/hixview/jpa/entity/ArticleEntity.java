package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = ARTICLE)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = PRESS_NUM_SNAKE, nullable = false)
    private PressEntity press;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

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

    private ArticleEntity(String name, String link, LocalDate date, String classification, String subjectCountry, String importance, String summary, PressEntity press, Long versionNumber) {
        this.name = name;
        this.link = link;
        this.date = date;
        this.classification = classification;
        this.subjectCountry = subjectCountry;
        this.importance = importance;
        this.summary = summary;
        this.press = press;
        this.versionNumber = versionNumber;
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
    }
}
