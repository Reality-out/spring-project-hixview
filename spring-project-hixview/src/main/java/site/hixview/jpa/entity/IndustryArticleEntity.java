package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDUSTRY_ARTICLE_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IndustryArticleEntity {
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = FIR_CATE_NUM_SNAKE)
    private FirstCategoryEntity firstCategory;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndustryArticleEntity industryArticle = (IndustryArticleEntity) obj;
        return new EqualsBuilder()
                .append(getArticle().getName(), industryArticle.getArticle().getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getArticle().getName()).toHashCode();
    }
}
