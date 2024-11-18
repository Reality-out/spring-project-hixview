package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordSnake.ARTI_NUM_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_ARTICLE_SNAKE;

@Entity
@Table(name = ECONOMY_ARTICLE_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EconomyArticleEntity {
    @OneToOne
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;
}
