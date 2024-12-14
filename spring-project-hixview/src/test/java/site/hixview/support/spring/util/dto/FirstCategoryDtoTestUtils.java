package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;
import site.hixview.support.spring.util.FirstCategoryTestUtils;

public interface FirstCategoryDtoTestUtils extends FirstCategoryTestUtils {
    /**
     * Create
     */
    default FirstCategoryDto createFirstCategoryDto() {
        FirstCategoryDto firstCategoryDto = new FirstCategoryDto();
        firstCategoryDto.setNumber(firstCategory.getNumber());
        firstCategoryDto.setKoreanName(firstCategory.getKoreanName());
        firstCategoryDto.setEnglishName(firstCategory.getEnglishName());
        firstCategoryDto.setIndustryCategoryNumber(firstCategory.getIndustryCategoryNumber());
        return firstCategoryDto;
    }

    default FirstCategoryDto createAnotherFirstCategoryDto() {
        FirstCategoryDto firstCategoryDto = new FirstCategoryDto();
        firstCategoryDto.setNumber(anotherFirstCategory.getNumber());
        firstCategoryDto.setKoreanName(anotherFirstCategory.getKoreanName());
        firstCategoryDto.setEnglishName(anotherFirstCategory.getEnglishName());
        firstCategoryDto.setIndustryCategoryNumber(anotherFirstCategory.getIndustryCategoryNumber());
        return firstCategoryDto;
    }

    default FirstCategoryDtoNoNumber createFirstCategoryDtoNoNumber() {
        FirstCategoryDtoNoNumber firstCategoryDto = new FirstCategoryDtoNoNumber();
        firstCategoryDto.setKoreanName(firstCategory.getKoreanName());
        firstCategoryDto.setEnglishName(firstCategory.getEnglishName());
        firstCategoryDto.setIndustryCategoryNumber(firstCategory.getIndustryCategoryNumber());
        return firstCategoryDto;
    }

    default FirstCategoryDtoNoNumber createAnotherFirstCategoryDtoNoNumber() {
        FirstCategoryDtoNoNumber firstCategoryDto = new FirstCategoryDtoNoNumber();
        firstCategoryDto.setKoreanName(anotherFirstCategory.getKoreanName());
        firstCategoryDto.setEnglishName(anotherFirstCategory.getEnglishName());
        firstCategoryDto.setIndustryCategoryNumber(anotherFirstCategory.getIndustryCategoryNumber());
        return firstCategoryDto;
    }
}