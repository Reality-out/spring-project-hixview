package site.hixview.aggregate.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;

@Getter
@EqualsAndHashCode(callSuper = false)
@Builder(access = AccessLevel.PUBLIC)
public class IndustryCategory implements ConvertibleToWholeDto<IndustryCategoryDto, IndustryCategoryDtoNoNumber> {

    private final Long number;
    private final String koreanName;
    private final String englishName;

    @Override
    public IndustryCategoryDto toDto() {
        IndustryCategoryDto industryCategoryDto = new IndustryCategoryDto();
        industryCategoryDto.setNumber(number);
        industryCategoryDto.setKoreanName(koreanName);
        industryCategoryDto.setEnglishName(englishName);
        return industryCategoryDto;
    }

    @Override
    public IndustryCategoryDtoNoNumber toDtoNoNumber() {
        IndustryCategoryDtoNoNumber industryCategoryDtoNoNumber = new IndustryCategoryDtoNoNumber();
        industryCategoryDtoNoNumber.setKoreanName(koreanName);
        industryCategoryDtoNoNumber.setEnglishName(englishName);
        return industryCategoryDtoNoNumber;
    }

    public static final class IndustryCategoryBuilder {
        private Long number;
        private String koreanName;
        private String englishName;

        public IndustryCategoryBuilder industryCategory(final IndustryCategory industryCategory) {
            this.number = industryCategory.getNumber();
            this.koreanName = industryCategory.getKoreanName();
            this.englishName = industryCategory.getEnglishName();
            return this;
        }

        public IndustryCategoryBuilder industryCategoryDto(final IndustryCategoryDto industryCategoryDto) {
            this.number = industryCategoryDto.getNumber();
            this.koreanName = industryCategoryDto.getKoreanName();
            this.englishName = industryCategoryDto.getEnglishName();
            return this;
        }

        public IndustryCategoryBuilder industryCategoryDtoNoNumber(final IndustryCategoryDtoNoNumber industryCategoryDtoNoNumber) {
            this.koreanName = industryCategoryDtoNoNumber.getKoreanName();
            this.englishName = industryCategoryDtoNoNumber.getEnglishName();
            return this;
        }
    }
}
