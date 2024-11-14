package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.EconomyContentDto;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class EconomyContent implements ConvertibleToDto<EconomyContentDto> {

    private final Long number;
    private final String name;

    @Override
    public EconomyContentDto toDto() {
        EconomyContentDto economyContentDto = new EconomyContentDto();
        economyContentDto.setNumber(number);
        economyContentDto.setName(name);
        return economyContentDto;
    }

    public static final class EconomyContentBuilder {
        private Long number;
        private String name;

        public EconomyContentBuilder economyContent(final EconomyContent economyContent) {
            this.number = economyContent.getNumber();
            this.name = economyContent.getName();
            return this;
        }

        public EconomyContentBuilder economyContentDto(final EconomyContentDto economyContentDto) {
            this.number = economyContentDto.getNumber();
            this.name = economyContentDto.getName();
            return this;
        }
    }
}
