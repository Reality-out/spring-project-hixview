package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = BLOG_POST_ARTICLE_MAPPER_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogPostArticleMapperEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = POST_NUM_SNAKE)
    private PostEntity post;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;
}
