package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.BatchSize;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.COMPANY_ARTICLE_SNAKE;

@Entity
@Table(name = COMPANY_ARTICLE_SNAKE)
@Getter
@NoArgsConstructor
public class CompanyArticleEntity {
    @Id
    private Long articleNumber;

    @OneToOne
    @MapsId
    @JoinColumn(name = NUM)
    @BatchSize(size = 200)
    private ArticleEntity article;

    public CompanyArticleEntity(ArticleEntity article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompanyArticleEntity companyArticle = (CompanyArticleEntity) obj;
        return new EqualsBuilder().append(getArticle().getName(), companyArticle.getArticle().getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getArticle().getName())
                .toHashCode();
    }
}
