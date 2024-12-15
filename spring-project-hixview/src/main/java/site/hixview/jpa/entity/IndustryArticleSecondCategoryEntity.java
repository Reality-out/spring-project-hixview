package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import site.hixview.jpa.entity.type.MapperTable;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDU_ARTI_SEC_CATE_MAPPER_SNAKE)
@MapperTable
@Getter
@NoArgsConstructor
public class IndustryArticleSecondCategoryEntity {
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

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndustryArticleSecondCategoryEntity industryArticleSecondCategoryMapper = (IndustryArticleSecondCategoryEntity) obj;
        return new EqualsBuilder()
                .append(getIndustryArticle().getName(), industryArticleSecondCategoryMapper.getIndustryArticle().getName())
                .append(getSecondCategory().getKoreanName(), industryArticleSecondCategoryMapper.getSecondCategory().getKoreanName())
                .append(getSecondCategory().getEnglishName(), industryArticleSecondCategoryMapper.getSecondCategory().getEnglishName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getIndustryArticle().getName())
                .append(getSecondCategory().getKoreanName())
                .append(getSecondCategory().getEnglishName())
                .toHashCode();
    }

    public IndustryArticleSecondCategoryEntity(Long number, IndustryArticleEntity industryArticle, SecondCategoryEntity secondCategory) {
        this.number = number;
        this.industryArticle = industryArticle;
        this.secondCategory = secondCategory;
    }

    public IndustryArticleSecondCategoryEntity(IndustryArticleEntity industryArticle, SecondCategoryEntity secondCategory, Long versionNumber) {
        this.industryArticle = industryArticle;
        this.secondCategory = secondCategory;
        this.versionNumber = versionNumber;
    }

    public IndustryArticleSecondCategoryEntity(IndustryArticleEntity industryArticle, SecondCategoryEntity secondCategory) {
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
