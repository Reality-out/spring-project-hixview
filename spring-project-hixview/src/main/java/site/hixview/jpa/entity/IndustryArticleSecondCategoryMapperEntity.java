package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDUSTRY_ARTICLE_SECOND_CATEGORY_MAPPER_SNAKE)
@Getter
public class IndustryArticleSecondCategoryMapperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = SEC_CATE_NUM_SNAKE)
    private SecondCategoryEntity secondCategory;

    public IndustryArticleSecondCategoryMapperEntity(ArticleEntity article, SecondCategoryEntity secondCategory) {
        this.article = article;
        this.secondCategory = secondCategory;
    }

    public void updateArticle(ArticleEntity article) {
        this.article = article;
    }

    public void updateSecondCategory(SecondCategoryEntity secondCategory) {
        this.secondCategory = secondCategory;
    }
}
