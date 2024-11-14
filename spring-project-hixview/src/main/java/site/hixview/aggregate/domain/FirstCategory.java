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
    private final Long categoryNumber;

    @Override
    public FirstCategoryDto toDto() {
        FirstCategoryDto firstCategoryDto = new FirstCategoryDto();
        firstCategoryDto.setNumber(number);
        firstCategoryDto.setKoreanName(koreanName);
        firstCategoryDto.setEnglishName(englishName);
        firstCategoryDto.setCategoryNumber(categoryNumber);
        return firstCategoryDto;
    }

    @Override
    public FirstCategoryDtoNoNumber toDtoNoNumber() {
        FirstCategoryDtoNoNumber firstCategoryDtoNoNumber = new FirstCategoryDtoNoNumber();
        firstCategoryDtoNoNumber.setKoreanName(koreanName);
        firstCategoryDtoNoNumber.setEnglishName(englishName);
        firstCategoryDtoNoNumber.setCategoryNumber(categoryNumber);
        return firstCategoryDtoNoNumber;
    }

    public static final class FirstCategoryBuilder {
        private Long number;
        private String koreanName;
        private String englishName;
        private Long categoryNumber;

        public FirstCategoryBuilder firstCategory(final FirstCategory firstCategory) {
            this.number = firstCategory.getNumber();
            this.koreanName = firstCategory.getKoreanName();
            this.englishName = firstCategory.getEnglishName();
            this.categoryNumber = firstCategory.getCategoryNumber();
            return this;
        }

        public FirstCategoryBuilder firstCategoryDto(final FirstCategoryDto firstCategoryDto) {
            this.number = firstCategoryDto.getNumber();
            this.koreanName = firstCategoryDto.getKoreanName();
            this.englishName = firstCategoryDto.getEnglishName();
            this.categoryNumber = firstCategoryDto.getCategoryNumber();
            return this;
        }

        public FirstCategoryBuilder firstCategoryDtoNoNumber(final FirstCategoryDtoNoNumber firstCategoryDtoNoNumber) {
            this.koreanName = firstCategoryDtoNoNumber.getKoreanName();
            this.englishName = firstCategoryDtoNoNumber.getEnglishName();
            this.categoryNumber = firstCategoryDtoNoNumber.getCategoryNumber();
            return this;
        }
    }
}
