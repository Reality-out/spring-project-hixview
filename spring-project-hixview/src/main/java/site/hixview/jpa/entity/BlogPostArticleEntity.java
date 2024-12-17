package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import site.hixview.jpa.entity.type.MapperTable;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = BLOG_POST_ARTI_MAPPER_SNAKE)
@MapperTable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = POST_NUM_SNAKE, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private BlogPostEntity blogPost;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = ARTI_NUM_SNAKE, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ArticleEntity article;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlogPostArticleEntity blogPostArticleMapper = (BlogPostArticleEntity) obj;
        return new EqualsBuilder()
                .append(getBlogPost().getName(), blogPostArticleMapper.getBlogPost().getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getBlogPost().getName())
                .toHashCode();
    }

    public BlogPostArticleEntity(Long number, BlogPostEntity blogPost, ArticleEntity article) {
        this.number = number;
        this.blogPost = blogPost;
        this.article = article;
    }

    public BlogPostArticleEntity(BlogPostEntity blogPost, ArticleEntity article, Long versionNumber) {
        this.blogPost = blogPost;
        this.article = article;
        this.versionNumber = versionNumber;
    }

    public BlogPostArticleEntity(BlogPostEntity blogPost, ArticleEntity article) {
        this.blogPost = blogPost;
        this.article = article;
    }

    public void updateBlogPost(BlogPostEntity post) {
        this.blogPost = post;
    }

    public void updateArticle(ArticleEntity article) {
        this.article = article;
    }
}
