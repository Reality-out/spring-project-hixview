package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMPANY_ARTICLE_COMPANY_MAPPER_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyArticleCompanyMapperEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = COMP_CODE_SNAKE)
    private CompanyEntity company;
}
