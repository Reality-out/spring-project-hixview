package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = ECONOMY_ARTICLE_CONTENT_MAPPER_SNAKE)
@Getter
public class EconomyArticleContentMapperEntity {
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
    @JoinColumn(name = ECON_CONT_NUM_SNAKE)
    private EconomyContentEntity economyContent;

    public EconomyArticleContentMapperEntity(ArticleEntity article, EconomyContentEntity economyContent) {
        this.article = article;
        this.economyContent = economyContent;
    }

    public void updateArticle(ArticleEntity article) {
        this.article = article;
    }

    public void updateEconomyContent(EconomyContentEntity economyContent) {
        this.economyContent = economyContent;
    }
}
