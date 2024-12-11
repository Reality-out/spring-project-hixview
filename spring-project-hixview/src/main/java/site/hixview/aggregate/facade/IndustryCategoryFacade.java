package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.domain.IndustryCategory.IndustryCategoryBuilder;
import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;

public class IndustryCategoryFacade {
    public IndustryCategoryBuilder createBuilder(IndustryCategory industryCategory) {
        return IndustryCategory.builder()
                .number(industryCategory.getNumber())
                .koreanName(industryCategory.getKoreanName())
                .englishName(industryCategory.getEnglishName());
    }

    public IndustryCategoryBuilder createBuilder(IndustryCategoryDto industryCategoryDto) {
        return IndustryCategory.builder()
                .number(industryCategoryDto.getNumber())
                .koreanName(industryCategoryDto.getKoreanName())
                .englishName(industryCategoryDto.getEnglishName());
    }

    public IndustryCategoryBuilder createBuilder(IndustryCategoryDtoNoNumber industryCategoryDto) {
        return IndustryCategory.builder()
                .koreanName(industryCategoryDto.getKoreanName())
                .englishName(industryCategoryDto.getEnglishName());
    }
}
