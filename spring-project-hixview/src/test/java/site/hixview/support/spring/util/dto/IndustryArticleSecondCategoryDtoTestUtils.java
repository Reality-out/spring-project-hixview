package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;

public interface IndustryArticleSecondCategoryDtoTestUtils {
    /**
     * Create
     */
    default IndustryArticleSecondCategoryDto createIndustryArticleSecondCategoryDto() {
        IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto = new IndustryArticleSecondCategoryDto();
        industryArticleSecondCategoryDto.setNumber(1L);
        industryArticleSecondCategoryDto.setArticleNumber(1L);
        industryArticleSecondCategoryDto.setSecondCategoryNumber(1L);
        return industryArticleSecondCategoryDto;
    }

    default IndustryArticleSecondCategoryDto createAnotherIndustryArticleSecondCategoryDto() {
        IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto = new IndustryArticleSecondCategoryDto();
        industryArticleSecondCategoryDto.setNumber(2L);
        industryArticleSecondCategoryDto.setArticleNumber(2L);
        industryArticleSecondCategoryDto.setSecondCategoryNumber(2L);
        return industryArticleSecondCategoryDto;
    }
}
