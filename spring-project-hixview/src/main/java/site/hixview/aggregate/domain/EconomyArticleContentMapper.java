package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.EconomyArticleContentMapperDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class EconomyArticleContentMapper implements ConvertibleToDto<EconomyArticleContentMapperDto> {
    private final Long number;
    private final Long articleNumber;
    private final Long contentNumber;

    @Override
    public EconomyArticleContentMapperDto toDto() {
        EconomyArticleContentMapperDto mapperDto = new EconomyArticleContentMapperDto();
        mapperDto.setNumber(number);
        mapperDto.setArticleNumber(articleNumber);
        mapperDto.setContentNumber(contentNumber);
        return mapperDto;
    }

    public static final class EconomyArticleContentMapperBuilder {
        private Long number;
        private Long articleNumber;
        private Long contentNumber;

        public EconomyArticleContentMapper.EconomyArticleContentMapperBuilder mapper(final EconomyArticleContentMapper mapper) {
            this.number = mapper.getNumber();
            this.articleNumber = mapper.getArticleNumber();
            this.contentNumber = mapper.getContentNumber();
            return this;
        }

        public EconomyArticleContentMapper.EconomyArticleContentMapperBuilder mapperDto(final EconomyArticleContentMapperDto mapperDto) {
            this.number = mapperDto.getNumber();
            this.articleNumber = mapperDto.getArticleNumber();
            this.contentNumber = mapperDto.getContentNumber();
            return this;
        }
    }
}
