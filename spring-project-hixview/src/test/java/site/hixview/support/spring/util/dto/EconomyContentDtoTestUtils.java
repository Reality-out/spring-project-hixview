package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.EconomyContentDto;

public interface EconomyContentDtoTestUtils {
    /**
     * Create
     */
    default EconomyContentDto createEconomyContentDto() {
        EconomyContentDto economyContentDto = new EconomyContentDto();
        economyContentDto.setNumber(1L);
        economyContentDto.setName("합병");
        return economyContentDto;
    }

    default EconomyContentDto createAnotherEconomyContentDto() {
        EconomyContentDto economyContentDto = new EconomyContentDto();
        economyContentDto.setNumber(2L);
        economyContentDto.setName("대주주");
        return economyContentDto;
    }
}
