package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import site.hixview.jpa.entity.supers.SuperPostEntity;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.BLOG_POST_SNAKE;

@Entity
@Table(name = BLOG_POST_SNAKE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogPostEntity extends SuperPostEntity {
    @Id
    private Long number;

    @OneToOne
    @MapsId
    @JoinColumn(name = NUM)
    private PostEntity post;

    @Column(nullable = false)
    private String classification;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlogPostEntity post = (BlogPostEntity) obj;
        return new EqualsBuilder().append(getName(), post.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }

    public static BlogPostEntityBuilder builder() {
        return new BlogPostEntityBuilder();
    }

    private BlogPostEntity(PostEntity post, String name, String link, LocalDate date, String classification) {
        super(name, link, date);
        this.post = post;
        this.classification = classification;
    }

    public static final class BlogPostEntityBuilder {
        private PostEntity post;
        private String name;
        private String link;
        private LocalDate date;
        private String classification;

        public BlogPostEntityBuilder post(final PostEntity post) {
            this.post = post;
            return this;
        }

        public BlogPostEntityBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public BlogPostEntityBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public BlogPostEntityBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public BlogPostEntityBuilder classification(final String classification) {
            this.classification = classification;
            return this;
        }

        public BlogPostEntityBuilder blogPost(final BlogPostEntity blogPost) {
            this.post = blogPost.getPost();
            this.name = blogPost.getName();
            this.link = blogPost.getLink();
            this.date = blogPost.getDate();
            this.classification = blogPost.getClassification();
            return this;
        }

        public BlogPostEntity build() {
            return new BlogPostEntity(this.post, this.name, this.link, this.date, this.classification);
        }
    }
}
