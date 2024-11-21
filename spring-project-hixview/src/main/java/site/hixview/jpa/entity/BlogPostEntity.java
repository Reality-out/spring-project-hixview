package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.POST_NUM_SNAKE;

@Entity
@Table(name = BLOG_POST_SNAKE)
@Getter
@NoArgsConstructor
public class BlogPostEntity {
    @Id
    private Long postNumber;

    @OneToOne
    @MapsId
    @JoinColumn(name = POST_NUM_SNAKE)
    private PostEntity post;

    @Column(nullable = false)
    private String classification;

    public BlogPostEntity(PostEntity post, String classification) {
        this.post = post;
        this.classification = classification;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlogPostEntity blogPost = (BlogPostEntity) obj;
        return new EqualsBuilder().append(getPost().getName(), blogPost.getPost().getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getPost().getName()).toHashCode();
    }
}
