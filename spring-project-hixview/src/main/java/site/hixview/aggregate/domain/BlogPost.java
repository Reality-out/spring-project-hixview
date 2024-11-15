package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;
import site.hixview.aggregate.enums.Classification;

import java.time.LocalDate;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPost implements ConvertibleToWholeDto<BlogPostDto, BlogPostDtoNoNumber> {

    private final Post post;
    private final Classification classification;

    @Override
    public BlogPostDto toDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setNumber(post.getNumber());
        blogPostDto.setName(post.getName());
        blogPostDto.setLink(post.getLink());
        blogPostDto.setDate(String.valueOf(post.getDate()));
        blogPostDto.setClassification(classification.name());
        return blogPostDto;
    }

    @Override
    public BlogPostDtoNoNumber toDtoNoNumber() {
        BlogPostDtoNoNumber blogPostDto = new BlogPostDtoNoNumber();
        blogPostDto.setName(post.getName());
        blogPostDto.setLink(post.getLink());
        blogPostDto.setDate(String.valueOf(post.getDate()));
        blogPostDto.setClassification(classification.name());
        return blogPostDto;
    }

    private BlogPost(final Long number, final String name, final String link, final LocalDate date, final Classification classification) {
        post = Post.builder().number(number).name(name).link(link).date(date).build();
        this.classification = classification;
    }

    public static final class BlogPostBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;
        private Classification classification;

        public BlogPostBuilder number(final Long number) {
            this.number = number;
            return this;
        }

        public BlogPostBuilder name(final String name) {
            this.name = name;
            return this;
        }
        
        public BlogPostBuilder link(final String link) {
            this.link = link;
            return this;
        }

        public BlogPostBuilder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public BlogPostBuilder post(final BlogPost blogPost) {
            this.number = blogPost.getPost().getNumber();
            this.name = blogPost.getPost().getName();
            this.link = blogPost.getPost().getLink();
            this.date = blogPost.getPost().getDate();
            this.classification = blogPost.getClassification();
            return this;
        }

        public BlogPostBuilder blogPostDto(final BlogPostDto blogPostDto) {
            this.number = blogPostDto.getNumber();
            this.name = blogPostDto.getName();
            this.link = blogPostDto.getLink();
            this.date = convertFromStringToLocalDate(blogPostDto.getDate());
            this.classification = Classification.valueOf(blogPostDto.getClassification());
            return this;
        }

        public BlogPostBuilder blogPostDto(final BlogPostDtoNoNumber blogPostDtoNoNumber) {
            this.name = blogPostDtoNoNumber.getName();
            this.link = blogPostDtoNoNumber.getLink();
            this.date = convertFromStringToLocalDate(blogPostDtoNoNumber.getDate());
            this.classification = Classification.valueOf(blogPostDtoNoNumber.getClassification());
            return this;
        }

        public BlogPost build() {
            return new BlogPost(this.number, this.name, this.link, this.date, this.classification);
        }
    }
}
