package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMPANY_ARTICLE_COMPANY_MAPPER_SNAKE)
@Getter
public class CompanyArticleCompanyMapperEntity {
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
    @JoinColumn(name = COMP_CODE_SNAKE)
    private CompanyEntity company;

    public CompanyArticleCompanyMapperEntity(ArticleEntity article, CompanyEntity company) {
        this.article = article;
        this.company = company;
    }

    public void updateArticle(ArticleEntity article) {
        this.article = article;
    }

    public void updateCompany(CompanyEntity company) {
        this.company = company;
    }
}
