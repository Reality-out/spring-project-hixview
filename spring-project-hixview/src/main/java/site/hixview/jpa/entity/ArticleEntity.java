package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = PRESS_NUM_SNAKE, nullable = false)
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
