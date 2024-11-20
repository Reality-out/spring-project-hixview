package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDUSTRY_ARTICLE_SNAKE)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IndustryArticleEntity {
    @Id
    private Long articleNumber;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = FIR_CATE_NUM_SNAKE, nullable = false)
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
