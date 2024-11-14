package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.enums.Classification;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPost implements ConvertibleToDto<BlogPostDto> {

    private final Long number;
    private final Classification classification;

    @Override
    public BlogPostDto toDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setNumber(number);
        blogPostDto.setClassification(classification.name());
        return blogPostDto;
    }

    public static final class BlogPostBuilder {
        private Long number;
        private Classification classification;

        public BlogPostBuilder blogPost(final BlogPost blogPost) {
            this.number = blogPost.getNumber();
            this.classification = blogPost.getClassification();
            return this;
        }

        public BlogPostBuilder blogPostDto(final BlogPostDto blogPostDto) {
            this.number = blogPostDto.getNumber();
            this.classification = Classification.valueOf(blogPostDto.getClassification());
            return this;
        }
    }
}
