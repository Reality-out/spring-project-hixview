package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;

public interface IndustryCategoryDtoTestUtils {
    /**
     * Create
     */
    default IndustryCategoryDto createIndustryCategoryDto() {
        IndustryCategoryDto industryCategoryDto = new IndustryCategoryDto();
        industryCategoryDto.setNumber(1L);
        industryCategoryDto.setKoreanName("1차 업종");
        industryCategoryDto.setEnglishName("FIRST_CATEGORY");
        return industryCategoryDto;
    }

    default IndustryCategoryDto createAnotherIndustryCategoryDto() {
        IndustryCategoryDto industryCategoryDto = new IndustryCategoryDto();
        industryCategoryDto.setNumber(2L);
        industryCategoryDto.setKoreanName("2차 업종");
        industryCategoryDto.setEnglishName("SECOND_CATEGORY");
        return industryCategoryDto;
    }

    default IndustryCategoryDtoNoNumber createIndustryCategoryDtoNoNumber() {
        IndustryCategoryDtoNoNumber industryCategoryDto = new IndustryCategoryDtoNoNumber();
        industryCategoryDto.setKoreanName("1차 업종");
        industryCategoryDto.setEnglishName("FIRST_CATEGORY");
        return industryCategoryDto;
    }

    default IndustryCategoryDtoNoNumber createAnotherIndustryCategoryDtoNoNumber() {
        IndustryCategoryDtoNoNumber industryCategoryDto = new IndustryCategoryDtoNoNumber();
        industryCategoryDto.setKoreanName("2차 업종");
        industryCategoryDto.setEnglishName("SECOND_CATEGORY");
        return industryCategoryDto;
    }
}
