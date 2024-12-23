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
import static site.hixview.aggregate.vo.WordSnake.FIR_CATE_NUM_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.INDUSTRY_ARTICLE_SNAKE;

@Entity
@Table(name = INDUSTRY_ARTICLE_SNAKE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndustryArticleEntity extends SuperArticleEntity {
    @Id
    private Long number;

    @OneToOne
    @MapsId
    @JoinColumn(name = NUM, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @BatchSize(size = 200)
    private ArticleEntity article;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = FIR_CATE_NUM_SNAKE, nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private FirstCategoryEntity firstCategory;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndustryArticleEntity industryArticle = (IndustryArticleEntity) obj;
        return new EqualsBuilder().append(getName(), industryArticle.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }

    public void updateFirstCategory(FirstCategoryEntity firstCategory) {
        this.firstCategory = firstCategory;
    }

    public static IndustryArticleEntityBuilder builder() {
        return new IndustryArticleEntityBuilder();
    }

    private IndustryArticleEntity(ArticleEntity article, String name, String link, LocalDate date, String subjectCountry, String importance, String summary, PressEntity press, FirstCategoryEntity firstCategory) {
        super(name, link, date, subjectCountry, importance, summary, press);
        this.article = article;
        this.firstCategory = firstCategory;
    }

    public static final class IndustryArticleEntityBuilder {
        private ArticleEntity article;
        private String name;
        private String link;
        private LocalDate date;
        private String subjectCountry;
        private String importance;
        private String summary;
        private PressEntity press;
        private FirstCategoryEntity firstCategory;

        public IndustryArticleEntityBuilder article(final ArticleEntity article) {
            this.article = article;
            return this;
        }

        public IndustryArticleEntityBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public IndustryArticleEntityBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public IndustryArticleEntityBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public IndustryArticleEntityBuilder subjectCountry(final String subjectCountry) {
            this.subjectCountry = subjectCountry;
            return this;
        }

        public IndustryArticleEntityBuilder importance(final String importance) {
            this.importance = importance;
            return this;
        }

        public IndustryArticleEntityBuilder summary(final String summary) {
            this.summary = summary;
            return this;
        }

        public IndustryArticleEntityBuilder press(final PressEntity press) {
            this.press = press;
            return this;
        }

        public IndustryArticleEntityBuilder firstCategory(final FirstCategoryEntity firstCategory) {
            this.firstCategory = firstCategory;
            return this;
        }

        public IndustryArticleEntityBuilder industryArticle(final IndustryArticleEntity industryArticle) {
            this.article = industryArticle.getArticle();
            this.name = industryArticle.getName();
            this.link = industryArticle.getLink();
            this.date = industryArticle.getDate();
            this.subjectCountry = industryArticle.getSubjectCountry();
            this.importance = industryArticle.getImportance();
            this.summary = industryArticle.getSummary();
            this.press = industryArticle.getPress();
            this.firstCategory = industryArticle.getFirstCategory();
            return this;
        }

        public IndustryArticleEntity build() {
            return new IndustryArticleEntity(this.article, this.name, this.link, this.date, this.subjectCountry, this.importance, this.summary, this.press, this.firstCategory);
        }
    }
}
