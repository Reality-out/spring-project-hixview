package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.enums.Classification;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPost {
    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Classification classification;
    private final List<Long> mappedArticleNumbers;

    public static class BlogPostBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Classification classification;
        private List<Long> mappedArticleNumbers;

        public BlogPostBuilder blogPost(final BlogPost blogPost) {
            this.number = blogPost.getNumber();
            this.name = blogPost.getName();
            this.link = blogPost.getLink();
            this.date = blogPost.getDate();
            this.classification = blogPost.getClassification();
            this.mappedArticleNumbers = blogPost.getMappedArticleNumbers();
            return this;
        }

        public BlogPost build() {
            return new BlogPost(this.number, this.name, this.link, this.date, this.classification, this.mappedArticleNumbers);
        }
    }
}