package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.ArticleDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class Article implements ConvertibleToDto<ArticleDto> {

    private final Long number;

    @Override
    public ArticleDto toDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setNumber(number);
        return articleDto;
    }

    public static final class ArticleBuilder {
        private Long number;

        public ArticleBuilder article(final Article article) {
            this.number = article.getNumber();
            return this;
        }

        public ArticleBuilder articleDto(final ArticleDto articleDto) {
            this.number = articleDto.getNumber();
            return this;
        }
    }
}
