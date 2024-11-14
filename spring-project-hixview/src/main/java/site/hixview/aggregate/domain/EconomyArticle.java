package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.EconomyArticleDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class EconomyArticle implements ConvertibleToDto<EconomyArticleDto> {

    private final Long number;

    @Override
    public EconomyArticleDto toDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setNumber(number);
        return economyArticleDto;
    }

    public static final class EconomyArticleBuilder {
        private Long number;

        public EconomyArticleBuilder economyArticle(final EconomyArticle economyArticle) {
            this.number = economyArticle.getNumber();
            return this;
        }

        public EconomyArticleBuilder economyArticleDto(final EconomyArticleDto economyArticleDto) {
            this.number = economyArticleDto.getNumber();
            return this;
        }
    }
}
