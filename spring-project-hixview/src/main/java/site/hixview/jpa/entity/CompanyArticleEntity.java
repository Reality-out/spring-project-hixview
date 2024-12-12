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
import static site.hixview.aggregate.vo.WordSnake.COMPANY_ARTICLE_SNAKE;

@Entity
@Table(name = COMPANY_ARTICLE_SNAKE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyArticleEntity extends SuperArticleEntity {
    @Id
    private Long number;

    @OneToOne
    @MapsId
    @JoinColumn(name = NUM)
    @BatchSize(size = 200)
    private ArticleEntity article;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompanyArticleEntity companyArticle = (CompanyArticleEntity) obj;
        return new EqualsBuilder().append(getName(), companyArticle.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }

    public static CompanyArticleEntityBuilder builder() {
        return new CompanyArticleEntityBuilder();
    }

    private CompanyArticleEntity(ArticleEntity article, String name, String link, LocalDate date, String subjectCountry, String importance, String summary, PressEntity press) {
        super(name, link, date, subjectCountry, importance, summary, press);
        this.article = article;
    }

    public static final class CompanyArticleEntityBuilder {
        private ArticleEntity article;
        private String name;
        private String link;
        private LocalDate date;
        private String subjectCountry;
        private String importance;
        private String summary;
        private PressEntity press;

        public CompanyArticleEntityBuilder article(final ArticleEntity article) {
            this.article = article;
            return this;
        }

        public CompanyArticleEntityBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public CompanyArticleEntityBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public CompanyArticleEntityBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public CompanyArticleEntityBuilder subjectCountry(final String subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public CompanyArticleEntityBuilder importance(final String importance) {
            this.importance = importance;
            return this;
        }

        public CompanyArticleEntityBuilder summary(final String summary) {
            this.summary = summary;
            return this;
        }

        public CompanyArticleEntityBuilder press(final PressEntity press) {
            this.press = press;
            return this;
        }

        public CompanyArticleEntityBuilder companyArticle(final CompanyArticleEntity industryArticle) {
            this.article = industryArticle.getArticle();
            this.name = industryArticle.getName();
            this.link = industryArticle.getLink();
            this.date = industryArticle.getDate();
            this.subjectCountry = industryArticle.getSubjectCountry();
            this.importance = industryArticle.getImportance();
            this.summary = industryArticle.getSummary();
            this.press = industryArticle.getPress();
            return this;
        }

        public CompanyArticleEntity build() {
            return new CompanyArticleEntity(this.article, this.name, this.link, this.date, this.subjectCountry, this.importance, this.summary, this.press);
        }
    }
}
