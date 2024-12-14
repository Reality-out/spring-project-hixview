package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.EconomyContentDto;
import site.hixview.support.spring.util.EconomyContentTestUtils;

public interface EconomyContentDtoTestUtils extends EconomyContentTestUtils {
    /**
     * Create
     */
    default EconomyContentDto createEconomyContentDto() {
        EconomyContentDto economyContentDto = new EconomyContentDto();
        economyContentDto.setNumber(economyContent.getNumber());
        economyContentDto.setName(economyContent.getName());
        return economyContentDto;
    }

    default EconomyContentDto createAnotherEconomyContentDto() {
        EconomyContentDto economyContentDto = new EconomyContentDto();
        economyContentDto.setNumber(anotherEconomyContent.getNumber());
        economyContentDto.setName(anotherEconomyContent.getName());
        return economyContentDto;
    }
}
