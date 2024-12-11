package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.domain.FirstCategory.FirstCategoryBuilder;
import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;

public class FirstCategoryFacade {
    public FirstCategoryBuilder createBuilder(FirstCategory firstCategory) {
        return FirstCategory.builder()
                .number(firstCategory.getNumber())
                .koreanName(firstCategory.getKoreanName())
                .englishName(firstCategory.getEnglishName())
                .industryCategoryNumber(firstCategory.getIndustryCategoryNumber());
    }

    public FirstCategoryBuilder createBuilder(FirstCategoryDto firstCategoryDto) {
        return FirstCategory.builder()
                .number(firstCategoryDto.getNumber())
                .koreanName(firstCategoryDto.getKoreanName())
                .englishName(firstCategoryDto.getEnglishName())
                .industryCategoryNumber(firstCategoryDto.getIndustryCategoryNumber());
    }

    public FirstCategoryBuilder createBuilder(FirstCategoryDtoNoNumber firstCategoryDto) {
        return FirstCategory.builder()
                .koreanName(firstCategoryDto.getKoreanName())
                .englishName(firstCategoryDto.getEnglishName())
                .industryCategoryNumber(firstCategoryDto.getIndustryCategoryNumber());
    }
}
