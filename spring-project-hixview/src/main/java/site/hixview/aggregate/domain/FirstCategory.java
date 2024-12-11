package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class FirstCategory implements ConvertibleToWholeDto<FirstCategoryDto, FirstCategoryDtoNoNumber> {

    private final Long number;
    private final String koreanName;
    private final String englishName;
    private final Long industryCategoryNumber;

    @Override
    public FirstCategoryDto toDto() {
        FirstCategoryDto firstCategoryDto = new FirstCategoryDto();
        firstCategoryDto.setNumber(number);
        firstCategoryDto.setKoreanName(koreanName);
        firstCategoryDto.setEnglishName(englishName);
        firstCategoryDto.setIndustryCategoryNumber(industryCategoryNumber);
        return firstCategoryDto;
    }

    @Override
    public FirstCategoryDtoNoNumber toDtoNoNumber() {
        FirstCategoryDtoNoNumber firstCategoryDto = new FirstCategoryDtoNoNumber();
        firstCategoryDto.setKoreanName(koreanName);
        firstCategoryDto.setEnglishName(englishName);
        firstCategoryDto.setIndustryCategoryNumber(industryCategoryNumber);
        return firstCategoryDto;
    }

    public static final class FirstCategoryBuilder {
        private Long number;
        private String koreanName;
        private String englishName;
        private Long industryCategoryNumber;

        public FirstCategoryBuilder firstCategory(final FirstCategory firstCategory) {
            this.number = firstCategory.getNumber();
            this.koreanName = firstCategory.getKoreanName();
            this.englishName = firstCategory.getEnglishName();
            this.industryCategoryNumber = firstCategory.getIndustryCategoryNumber();
            return this;
        }

        public FirstCategoryBuilder firstCategoryDto(final FirstCategoryDto firstCategoryDto) {
            this.number = firstCategoryDto.getNumber();
            this.koreanName = firstCategoryDto.getKoreanName();
            this.englishName = firstCategoryDto.getEnglishName();
            this.industryCategoryNumber = firstCategoryDto.getIndustryCategoryNumber();
            return this;
        }

        public FirstCategoryBuilder firstCategoryDtoNoNumber(final FirstCategoryDtoNoNumber firstCategoryDto) {
            this.koreanName = firstCategoryDto.getKoreanName();
            this.englishName = firstCategoryDto.getEnglishName();
            this.industryCategoryNumber = firstCategoryDto.getIndustryCategoryNumber();
            return this;
        }
    }
}
