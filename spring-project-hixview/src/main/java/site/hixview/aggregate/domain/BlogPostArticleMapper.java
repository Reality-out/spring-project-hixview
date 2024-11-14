package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.BlogPostArticleMapperDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPostArticleMapper implements ConvertibleToDto<BlogPostArticleMapperDto> {
    private final Long number;
    private final Long postNumber;
    private final Long articleNumber;

    @Override
    public BlogPostArticleMapperDto toDto() {
        BlogPostArticleMapperDto mapperDto = new BlogPostArticleMapperDto();
        mapperDto.setNumber(number);
        mapperDto.setPostNumber(postNumber);
        mapperDto.setArticleNumber(articleNumber);
        return mapperDto;
    }

    public static final class BlogPostArticleMapperBuilder {
        private Long number;
        private Long postNumber;
        private Long articleNumber;

        public BlogPostArticleMapper.BlogPostArticleMapperBuilder mapper(final BlogPostArticleMapper mapper) {
            this.number = mapper.getNumber();
            this.postNumber = mapper.getPostNumber();
            this.articleNumber = mapper.getArticleNumber();
            return this;
        }

        public BlogPostArticleMapper.BlogPostArticleMapperBuilder mapperDto(final BlogPostArticleMapperDto mapperDto) {
            this.number = mapperDto.getNumber();
            this.postNumber = mapperDto.getPostNumber();
            this.articleNumber = mapperDto.getArticleNumber();
            return this;
        }
    }
}
