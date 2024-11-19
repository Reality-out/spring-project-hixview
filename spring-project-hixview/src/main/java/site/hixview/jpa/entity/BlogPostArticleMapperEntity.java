package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = BLOG_POST_ARTICLE_MAPPER_SNAKE)
@Getter
public class BlogPostArticleMapperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = POST_NUM_SNAKE)
    private PostEntity post;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    public BlogPostArticleMapperEntity(PostEntity post, ArticleEntity article) {
        this.post = post;
        this.article = article;
    }

    public void updatePost(PostEntity post) {
        this.post = post;
    }

    public void updateArticle(ArticleEntity article) {
        this.article = article;
    }
}
