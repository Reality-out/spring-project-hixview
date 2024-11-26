package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = BLOG_POST_ARTI_MAPPER_SNAKE)
@Getter
@NoArgsConstructor
public class BlogPostArticleMapperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = POST_NUM_SNAKE)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlogPostArticleMapperEntity blogPostArticleMapper = (BlogPostArticleMapperEntity) obj;
        return new EqualsBuilder()
                .append(getPost().getName(), blogPostArticleMapper.getPost().getName())
                .append(getArticle().getName(), blogPostArticleMapper.getArticle().getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getPost().getName())
                .append(getArticle().getName())
                .toHashCode();
    }

    public BlogPostArticleMapperEntity(PostEntity post, ArticleEntity article, Long versionNumber) {
        this.post = post;
        this.article = article;
        this.versionNumber = versionNumber;
    }

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
