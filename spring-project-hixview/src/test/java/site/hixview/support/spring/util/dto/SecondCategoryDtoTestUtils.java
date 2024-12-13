package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;

public interface SecondCategoryDtoTestUtils {
    /**
     * Create
     */
    default SecondCategoryDto createSecondCategoryDto() {
        SecondCategoryDto secondCategoryDto = new SecondCategoryDto();
        secondCategoryDto.setNumber(1L);
        secondCategoryDto.setKoreanName("은행");
        secondCategoryDto.setEnglishName("BANK");
        secondCategoryDto.setIndustryCategoryNumber(2L);
        secondCategoryDto.setFirstCategoryNumber(1L);
        return secondCategoryDto;
    }

    default SecondCategoryDto createAnotherSecondCategoryDto() {
        SecondCategoryDto secondCategoryDto = new SecondCategoryDto();
        secondCategoryDto.setNumber(1L);
        secondCategoryDto.setKoreanName("배터리 제조");
        secondCategoryDto.setEnglishName("BATTERY_MANUFACTURING");
        secondCategoryDto.setIndustryCategoryNumber(2L);
        secondCategoryDto.setFirstCategoryNumber(2L);
        return secondCategoryDto;
    }

    default SecondCategoryDtoNoNumber createSecondCategoryDtoNoNumber() {
        SecondCategoryDtoNoNumber secondCategoryDto = new SecondCategoryDtoNoNumber();
        secondCategoryDto.setKoreanName("은행");
        secondCategoryDto.setEnglishName("BANK");
        secondCategoryDto.setIndustryCategoryNumber(2L);
        secondCategoryDto.setFirstCategoryNumber(1L);
        return secondCategoryDto;
    }

    default SecondCategoryDtoNoNumber createAnotherSecondCategoryDtoNoNumber() {
        SecondCategoryDtoNoNumber secondCategoryDto = new SecondCategoryDtoNoNumber();
        secondCategoryDto.setKoreanName("배터리 제조");
        secondCategoryDto.setEnglishName("BATTERY_MANUFACTURING");
        secondCategoryDto.setIndustryCategoryNumber(2L);
        secondCategoryDto.setFirstCategoryNumber(2L);
        return secondCategoryDto;
    }
}