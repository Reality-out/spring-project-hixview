package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDU_ARTI_SEC_CATE_MAPPER_SNAKE)
@Getter
@NoArgsConstructor
public class IndustryArticleSecondCategoryMapperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private IndustryArticleEntity industryArticle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = SEC_CATE_NUM_SNAKE)
    private SecondCategoryEntity secondCategory;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndustryArticleSecondCategoryMapperEntity industryArticleSecondCategoryMapper = (IndustryArticleSecondCategoryMapperEntity) obj;
        return new EqualsBuilder()
                .append(getIndustryArticle().getArticle().getName(), industryArticleSecondCategoryMapper.getIndustryArticle().getArticle().getName())
                .append(getSecondCategory().getKoreanName(), industryArticleSecondCategoryMapper.getSecondCategory().getKoreanName())
                .append(getSecondCategory().getEnglishName(), industryArticleSecondCategoryMapper.getSecondCategory().getEnglishName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getIndustryArticle().getArticle().getName())
                .append(getSecondCategory().getKoreanName())
                .append(getSecondCategory().getEnglishName())
                .toHashCode();
    }

    public IndustryArticleSecondCategoryMapperEntity(IndustryArticleEntity industryArticle, SecondCategoryEntity secondCategory) {
        this.industryArticle = industryArticle;
        this.secondCategory = secondCategory;
    }

    public void updateArticle(IndustryArticleEntity article) {
        this.industryArticle = article;
    }

    public void updateSecondCategory(SecondCategoryEntity secondCategory) {
        this.secondCategory = secondCategory;
    }
}
