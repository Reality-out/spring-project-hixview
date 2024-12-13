package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;

public interface FirstCategoryDtoTestUtils {
    /**
     * Create
     */
    default FirstCategoryDto createFirstCategoryDto() {
        FirstCategoryDto firstCategoryDto = new FirstCategoryDto();
        firstCategoryDto.setNumber(1L);
        firstCategoryDto.setKoreanName("건설");
        firstCategoryDto.setEnglishName("CONSTRUCTION");
        firstCategoryDto.setIndustryCategoryNumber(1L);
        return firstCategoryDto;
    }

    default FirstCategoryDto createAnotherFirstCategoryDto() {
        FirstCategoryDto firstCategoryDto = new FirstCategoryDto();
        firstCategoryDto.setNumber(1L);
        firstCategoryDto.setKoreanName("방산");
        firstCategoryDto.setEnglishName("DEFENSE");
        firstCategoryDto.setIndustryCategoryNumber(1L);
        return firstCategoryDto;
    }

    default FirstCategoryDtoNoNumber createFirstCategoryDtoNoNumber() {
        FirstCategoryDtoNoNumber firstCategoryDto = new FirstCategoryDtoNoNumber();
        firstCategoryDto.setKoreanName("건설");
        firstCategoryDto.setEnglishName("CONSTRUCTION");
        firstCategoryDto.setIndustryCategoryNumber(1L);
        return firstCategoryDto;
    }

    default FirstCategoryDtoNoNumber createAnotherFirstCategoryDtoNoNumber() {
        FirstCategoryDtoNoNumber firstCategoryDto = new FirstCategoryDtoNoNumber();
        firstCategoryDto.setKoreanName("방산");
        firstCategoryDto.setEnglishName("DEFENSE");
        firstCategoryDto.setIndustryCategoryNumber(1L);
        return firstCategoryDto;
    }
}