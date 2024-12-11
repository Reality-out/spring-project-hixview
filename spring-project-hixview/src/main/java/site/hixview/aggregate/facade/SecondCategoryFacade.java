package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.domain.SecondCategory.SecondCategoryBuilder;
import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;

public class SecondCategoryFacade {
    public SecondCategoryBuilder createBuilder(SecondCategory secondCategory) {
        return SecondCategory.builder()
                .number(secondCategory.getNumber())
                .koreanName(secondCategory.getKoreanName())
                .englishName(secondCategory.getEnglishName())
                .industryCategoryNumber(secondCategory.getIndustryCategoryNumber())
                .firstCategoryNumber(secondCategory.getFirstCategoryNumber());
    }

    public SecondCategoryBuilder createBuilder(SecondCategoryDto secondCategoryDto) {
        return SecondCategory.builder()
                .number(secondCategoryDto.getNumber())
                .koreanName(secondCategoryDto.getKoreanName())
                .englishName(secondCategoryDto.getEnglishName())
                .industryCategoryNumber(secondCategoryDto.getIndustryCategoryNumber())
                .firstCategoryNumber(secondCategoryDto.getFirstCategoryNumber());
    }

    public SecondCategoryBuilder createBuilder(SecondCategoryDtoNoNumber secondCategoryDto) {
        return SecondCategory.builder()
                .koreanName(secondCategoryDto.getKoreanName())
                .englishName(secondCategoryDto.getEnglishName())
                .industryCategoryNumber(secondCategoryDto.getIndustryCategoryNumber())
                .firstCategoryNumber(secondCategoryDto.getFirstCategoryNumber());
    }
}
