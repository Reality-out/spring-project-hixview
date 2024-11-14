package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class SecondCategory implements ConvertibleToWholeDto<SecondCategoryDto, SecondCategoryDtoNoNumber> {

    private final Long number;
    private final String koreanName;
    private final String englishName;
    private final Long categoryNumber;
    private final Long firstCategoryNumber;

    @Override
    public SecondCategoryDto toDto() {
        SecondCategoryDto secondCategoryDto = new SecondCategoryDto();
        secondCategoryDto.setNumber(number);
        secondCategoryDto.setKoreanName(koreanName);
        secondCategoryDto.setEnglishName(englishName);
        secondCategoryDto.setCategoryNumber(categoryNumber);
        secondCategoryDto.setFirstCategoryNumber(firstCategoryNumber);
        return secondCategoryDto;
    }

    @Override
    public SecondCategoryDtoNoNumber toDtoNoNumber() {
        SecondCategoryDtoNoNumber secondCategoryDtoNoNumber = new SecondCategoryDtoNoNumber();
        secondCategoryDtoNoNumber.setKoreanName(koreanName);
        secondCategoryDtoNoNumber.setEnglishName(englishName);
        secondCategoryDtoNoNumber.setCategoryNumber(categoryNumber);
        secondCategoryDtoNoNumber.setFirstCategoryNumber(firstCategoryNumber);
        return secondCategoryDtoNoNumber;
    }

    public static final class SecondCategoryBuilder {
        private Long number;
        private String koreanName;
        private String englishName;
        private Long categoryNumber;
        private Long firstCategoryNumber;

        public SecondCategoryBuilder secondCategory(final SecondCategory secondCategory) {
            this.number = secondCategory.getNumber();
            this.koreanName = secondCategory.getKoreanName();
            this.englishName = secondCategory.getEnglishName();
            this.categoryNumber = secondCategory.getCategoryNumber();
            this.firstCategoryNumber = secondCategory.getFirstCategoryNumber();
            return this;
        }

        public SecondCategoryBuilder secondCategoryDto(final SecondCategoryDto secondCategoryDto) {
            this.number = secondCategoryDto.getNumber();
            this.koreanName = secondCategoryDto.getKoreanName();
            this.englishName = secondCategoryDto.getEnglishName();
            this.categoryNumber = secondCategoryDto.getCategoryNumber();
            this.firstCategoryNumber = secondCategoryDto.getFirstCategoryNumber();
            return this;
        }

        public SecondCategoryBuilder secondCategoryDtoNoNumber(final SecondCategoryDtoNoNumber secondCategoryDtoNoNumber) {
            this.koreanName = secondCategoryDtoNoNumber.getKoreanName();
            this.englishName = secondCategoryDtoNoNumber.getEnglishName();
            this.categoryNumber = secondCategoryDtoNoNumber.getCategoryNumber();
            this.firstCategoryNumber = secondCategoryDtoNoNumber.getFirstCategoryNumber();
            return this;
        }
    }
}
