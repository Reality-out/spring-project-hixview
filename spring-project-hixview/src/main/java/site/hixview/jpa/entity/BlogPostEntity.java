package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.POST_NUM_SNAKE;

@Entity
@Table(name = BLOG_POST_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogPostEntity {
    @OneToOne
    @MapsId
    @JoinColumn(name = POST_NUM_SNAKE)
    private PostEntity post;

    @Column(nullable = false)
    private String classification;
}
