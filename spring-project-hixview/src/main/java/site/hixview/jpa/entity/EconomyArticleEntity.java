package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordSnake.ARTI_NUM_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_ARTICLE_SNAKE;

@Entity
@Table(name = ECONOMY_ARTICLE_SNAKE)
@Getter
@NoArgsConstructor
public class EconomyArticleEntity {
    @Id
    private Long articleNumber;

    @OneToOne
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    public EconomyArticleEntity(ArticleEntity article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EconomyArticleEntity economyArticle = (EconomyArticleEntity) obj;
        return new EqualsBuilder().append(getArticle().getName(), economyArticle.getArticle().getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getArticle().getName())
                .toHashCode();
    }
}
