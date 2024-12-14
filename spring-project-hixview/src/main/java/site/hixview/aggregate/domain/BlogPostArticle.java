package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPostArticle {
    private final Long number;
    private final Long postNumber;
    private final Long articleNumber;

    public static class BlogPostArticleBuilder {
        private Long number;
        private Long postNumber;
        private Long articleNumber;

        public BlogPostArticleBuilder blogPostArticle(final BlogPostArticle blogPostArticle) {
            this.number = blogPostArticle.getNumber();
            this.postNumber = blogPostArticle.getPostNumber();
            this.articleNumber = blogPostArticle.getArticleNumber();
            return this;
        }

        public BlogPostArticle build() {
            return new BlogPostArticle(this.number, this.postNumber, this.articleNumber);
        }
    }
}