package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.COMPANY_ARTICLE_SNAKE;

@Entity
@Table(name = COMPANY_ARTICLE_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyArticleEntity {
    @OneToOne
    @MapsId
    @JoinColumn(name = NUM)
    private ArticleEntity article;
}
