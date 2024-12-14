package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;
import site.hixview.support.spring.util.SecondCategoryTestUtils;

public interface SecondCategoryDtoTestUtils extends SecondCategoryTestUtils {
    /**
     * Create
     */
    default SecondCategoryDto createSecondCategoryDto() {
        SecondCategoryDto secondCategoryDto = new SecondCategoryDto();
        secondCategoryDto.setNumber(secondCategory.getNumber());
        secondCategoryDto.setKoreanName(secondCategory.getKoreanName());
        secondCategoryDto.setEnglishName(secondCategory.getEnglishName());
        secondCategoryDto.setIndustryCategoryNumber(secondCategory.getIndustryCategoryNumber());
        secondCategoryDto.setFirstCategoryNumber(secondCategory.getFirstCategoryNumber());
        return secondCategoryDto;
    }

    default SecondCategoryDto createAnotherSecondCategoryDto() {
        SecondCategoryDto secondCategoryDto = new SecondCategoryDto();
        secondCategoryDto.setNumber(anotherSecondCategory.getNumber());
        secondCategoryDto.setKoreanName(anotherSecondCategory.getKoreanName());
        secondCategoryDto.setEnglishName(anotherSecondCategory.getEnglishName());
        secondCategoryDto.setIndustryCategoryNumber(anotherSecondCategory.getIndustryCategoryNumber());
        secondCategoryDto.setFirstCategoryNumber(anotherSecondCategory.getFirstCategoryNumber());
        return secondCategoryDto;
    }

    default SecondCategoryDtoNoNumber createSecondCategoryDtoNoNumber() {
        SecondCategoryDtoNoNumber secondCategoryDto = new SecondCategoryDtoNoNumber();
        secondCategoryDto.setKoreanName(secondCategory.getKoreanName());
        secondCategoryDto.setEnglishName(secondCategory.getEnglishName());
        secondCategoryDto.setIndustryCategoryNumber(secondCategory.getIndustryCategoryNumber());
        secondCategoryDto.setFirstCategoryNumber(secondCategory.getFirstCategoryNumber());
        return secondCategoryDto;
    }

    default SecondCategoryDtoNoNumber createAnotherSecondCategoryDtoNoNumber() {
        SecondCategoryDtoNoNumber secondCategoryDto = new SecondCategoryDtoNoNumber();
        secondCategoryDto.setKoreanName(anotherSecondCategory.getKoreanName());
        secondCategoryDto.setEnglishName(anotherSecondCategory.getEnglishName());
        secondCategoryDto.setIndustryCategoryNumber(anotherSecondCategory.getIndustryCategoryNumber());
        secondCategoryDto.setFirstCategoryNumber(anotherSecondCategory.getFirstCategoryNumber());
        return secondCategoryDto;
    }
}