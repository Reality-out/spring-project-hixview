package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.EconomyArticleContentDto;
import site.hixview.support.spring.util.EconomyArticleContentTestUtils;

public interface EconomyArticleContentDtoTestUtils extends EconomyArticleContentTestUtils {
    /**
     * Create
     */
    default EconomyArticleContentDto createEconomyArticleContentDto() {
        EconomyArticleContentDto economyArticleContentDto = new EconomyArticleContentDto();
        economyArticleContentDto.setNumber(economyArticleContent.getNumber());
        economyArticleContentDto.setArticleNumber(economyArticleContent.getArticleNumber());
        economyArticleContentDto.setContentNumber(economyArticleContent.getContentNumber());
        return economyArticleContentDto;
    }

    default EconomyArticleContentDto createAnotherEconomyArticleContentDto() {
        EconomyArticleContentDto economyArticleContentDto = new EconomyArticleContentDto();
        economyArticleContentDto.setNumber(anotherEconomyArticleContent.getNumber());
        economyArticleContentDto.setArticleNumber(anotherEconomyArticleContent.getArticleNumber());
        economyArticleContentDto.setContentNumber(anotherEconomyArticleContent.getContentNumber());
        return economyArticleContentDto;
    }
}
