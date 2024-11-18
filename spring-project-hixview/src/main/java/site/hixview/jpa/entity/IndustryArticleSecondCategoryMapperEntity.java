package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDUSTRY_ARTICLE_SECOND_CATEGORY_MAPPER_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class IndustryArticleSecondCategoryMapperEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = SEC_CATE_NUM_SNAKE)
    private SecondCategoryEntity secondCategory;
}
