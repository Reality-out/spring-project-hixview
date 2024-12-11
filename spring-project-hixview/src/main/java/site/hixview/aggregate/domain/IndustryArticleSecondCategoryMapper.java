package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.IndustryArticleSecondCategoryMapperDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class IndustryArticleSecondCategoryMapper implements ConvertibleToDto<IndustryArticleSecondCategoryMapperDto> {
    private final Long number;
    private final Long articleNumber;
    private final Long secondCategoryNumber;

    @Override
    public IndustryArticleSecondCategoryMapperDto toDto() {
        IndustryArticleSecondCategoryMapperDto mapperDto = new IndustryArticleSecondCategoryMapperDto();
        mapperDto.setNumber(number);
        mapperDto.setArticleNumber(articleNumber);
        mapperDto.setSecondCategoryNumber(secondCategoryNumber);
        return mapperDto;
    }

    public static final class IndustryArticleSecondCategoryMapperBuilder {
        private Long number;
        private Long articleNumber;
        private Long contentNumber;

        public IndustryArticleSecondCategoryMapperBuilder mapper(final IndustryArticleSecondCategoryMapper mapper) {
            this.number = mapper.getNumber();
            this.articleNumber = mapper.getArticleNumber();
            this.contentNumber = mapper.getSecondCategoryNumber();
            return this;
        }

        public IndustryArticleSecondCategoryMapperBuilder mapperDto(final IndustryArticleSecondCategoryMapperDto mapperDto) {
            this.number = mapperDto.getNumber();
            this.articleNumber = mapperDto.getArticleNumber();
            this.contentNumber = mapperDto.getSecondCategoryNumber();
            return this;
        }
    }
}
