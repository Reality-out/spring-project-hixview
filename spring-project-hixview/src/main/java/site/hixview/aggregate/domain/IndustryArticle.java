package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.IndustryArticleDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class IndustryArticle implements ConvertibleToDto<IndustryArticleDto> {

    private final Long number;
    private final Long firstCategoryNumber;

    @Override
    public IndustryArticleDto toDto() {
        IndustryArticleDto industryArticleDto = new IndustryArticleDto();
        industryArticleDto.setNumber(number);
        industryArticleDto.setFirstCategoryNumber(firstCategoryNumber);
        return industryArticleDto;
    }

    public static final class IndustryArticleBuilder {
        private Long number;
        private Long firstCategoryNumber;

        public IndustryArticleBuilder industryArticle(final IndustryArticle industryArticle) {
            this.number = industryArticle.getNumber();
            this.firstCategoryNumber = industryArticle.getFirstCategoryNumber();
            return this;
        }

        public IndustryArticleBuilder industryArticleDto(final IndustryArticleDto industryArticleDto) {
            this.number = industryArticleDto.getNumber();
            this.firstCategoryNumber = industryArticleDto.getFirstCategoryNumber();
            return this;
        }
    }
}
