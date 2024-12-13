package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.EconomyArticleContentDto;

public interface EconomyArticleContentDtoTestUtils {
    /**
     * Create
     */
    default EconomyArticleContentDto createEconomyArticleContentDto() {
        EconomyArticleContentDto economyArticleContentDto = new EconomyArticleContentDto();
        economyArticleContentDto.setNumber(1L);
        economyArticleContentDto.setArticleNumber(1L);
        economyArticleContentDto.setContentNumber(1L);
        return economyArticleContentDto;
    }

    default EconomyArticleContentDto createAnotherEconomyArticleContentDto() {
        EconomyArticleContentDto economyArticleContentDto = new EconomyArticleContentDto();
        economyArticleContentDto.setNumber(2L);
        economyArticleContentDto.setArticleNumber(2L);
        economyArticleContentDto.setContentNumber(2L);
        return economyArticleContentDto;
    }
}
