package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.PressDto;
import site.hixview.aggregate.dto.PressDtoNoNumber;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder(access = AccessLevel.PUBLIC)
public class Press implements ConvertibleToWholeDto<PressDto, PressDtoNoNumber> {

    private final Long number;
    private final String koreanName;
    private final String englishName;

    @Override
    public PressDto toDto() {
        PressDto pressDto = new PressDto();
        pressDto.setNumber(number);
        pressDto.setKoreanName(koreanName);
        pressDto.setEnglishName(englishName);
        return pressDto;
    }

    @Override
    public PressDtoNoNumber toDtoNoNumber() {
        PressDtoNoNumber pressDto = new PressDtoNoNumber();
        pressDto.setKoreanName(koreanName);
        pressDto.setEnglishName(englishName);
        return pressDto;
    }

    public static final class PressBuilder {
        private Long number;
        private String koreanName;
        private String englishName;

        public PressBuilder press(final Press press) {
            this.number = press.getNumber();
            this.koreanName = press.getKoreanName();
            this.englishName = press.getEnglishName();
            return this;
        }

        public PressBuilder pressDto(final PressDto pressDto) {
            this.number = pressDto.getNumber();
            this.koreanName = pressDto.getKoreanName();
            this.englishName = pressDto.getEnglishName();
            return this;
        }

        public PressBuilder pressDtoNoNumber(final PressDtoNoNumber pressDto) {
            this.koreanName = pressDto.getKoreanName();
            this.englishName = pressDto.getEnglishName();
            return this;
        }
    }
}
