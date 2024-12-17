package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.BatchSize;
import site.hixview.jpa.entity.supers.SuperArticleEntity;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_ARTICLE_SNAKE;

@Entity
@Table(name = ECONOMY_ARTICLE_SNAKE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EconomyArticleEntity extends SuperArticleEntity {
    @Id
    private Long number;

    @OneToOne
    @MapsId
    @JoinColumn(name = NUM, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @BatchSize(size = 200)
    private ArticleEntity article;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EconomyArticleEntity economyArticle = (EconomyArticleEntity) obj;
        return new EqualsBuilder().append(getName(), economyArticle.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }
    
    public static EconomyArticleEntityBuilder builder() {
        return new EconomyArticleEntityBuilder();
    }

    private EconomyArticleEntity(ArticleEntity article, String name, String link, LocalDate date, String subjectCountry, String importance, String summary, PressEntity press) {
        super(name, link, date, subjectCountry, importance, summary, press);
        this.article = article;
    }

    public static final class EconomyArticleEntityBuilder {
        private ArticleEntity article;
        private String name;
        private String link;
        private LocalDate date;
        private String subjectCountry;
        private String importance;
        private String summary;
        private PressEntity press;

        public EconomyArticleEntityBuilder article(final ArticleEntity article) {
            this.article = article;
            return this;
        }

        public EconomyArticleEntityBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public EconomyArticleEntityBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public EconomyArticleEntityBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public EconomyArticleEntityBuilder subjectCountry(final String subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public EconomyArticleEntityBuilder importance(final String importance) {
            this.importance = importance;
            return this;
        }

        public EconomyArticleEntityBuilder summary(final String summary) {
            this.summary = summary;
            return this;
        }

        public EconomyArticleEntityBuilder press(final PressEntity press) {
            this.press = press;
            return this;
        }

        public EconomyArticleEntityBuilder economyArticle(final EconomyArticleEntity economyArticle) {
            this.article = economyArticle.getArticle();
            this.name = economyArticle.getName();
            this.link = economyArticle.getLink();
            this.date = economyArticle.getDate();
            this.subjectCountry = economyArticle.getSubjectCountry();
            this.importance = economyArticle.getImportance();
            this.summary = economyArticle.getSummary();
            this.press = economyArticle.getPress();
            return this;
        }

        public EconomyArticleEntity build() {
            return new EconomyArticleEntity(this.article, this.name, this.link, this.date, this.subjectCountry, this.importance, this.summary, this.press);
        }
    }
}
