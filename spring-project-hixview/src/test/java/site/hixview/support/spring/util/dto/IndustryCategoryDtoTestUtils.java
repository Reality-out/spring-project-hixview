package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;
import site.hixview.support.spring.util.IndustryCategoryTestUtils;

public interface IndustryCategoryDtoTestUtils extends IndustryCategoryTestUtils {
    /**
     * Create
     */
    default IndustryCategoryDto createIndustryCategoryDto() {
        IndustryCategoryDto industryCategoryDto = new IndustryCategoryDto();
        industryCategoryDto.setNumber(industryCategory.getNumber());
        industryCategoryDto.setKoreanName(industryCategory.getKoreanName());
        industryCategoryDto.setEnglishName(industryCategory.getEnglishName());
        return industryCategoryDto;
    }

    default IndustryCategoryDto createAnotherIndustryCategoryDto() {
        IndustryCategoryDto industryCategoryDto = new IndustryCategoryDto();
        industryCategoryDto.setNumber(anotherIndustryCategory.getNumber());
        industryCategoryDto.setKoreanName(anotherIndustryCategory.getKoreanName());
        industryCategoryDto.setEnglishName(anotherIndustryCategory.getEnglishName());
        return industryCategoryDto;
    }

    default IndustryCategoryDtoNoNumber createIndustryCategoryDtoNoNumber() {
        IndustryCategoryDtoNoNumber industryCategoryDto = new IndustryCategoryDtoNoNumber();
        industryCategoryDto.setKoreanName(industryCategory.getKoreanName());
        industryCategoryDto.setEnglishName(industryCategory.getEnglishName());
        return industryCategoryDto;
    }

    default IndustryCategoryDtoNoNumber createAnotherIndustryCategoryDtoNoNumber() {
        IndustryCategoryDtoNoNumber industryCategoryDto = new IndustryCategoryDtoNoNumber();
        industryCategoryDto.setKoreanName(anotherIndustryCategory.getKoreanName());
        industryCategoryDto.setEnglishName(anotherIndustryCategory.getEnglishName());
        return industryCategoryDto;
    }
}
