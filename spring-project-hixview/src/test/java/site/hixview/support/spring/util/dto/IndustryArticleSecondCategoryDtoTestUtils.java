package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;
import site.hixview.support.spring.util.IndustryArticleSecondCategoryTestUtils;

public interface IndustryArticleSecondCategoryDtoTestUtils extends IndustryArticleSecondCategoryTestUtils {
    /**
     * Create
     */
    default IndustryArticleSecondCategoryDto createIndustryArticleSecondCategoryDto() {
        IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto = new IndustryArticleSecondCategoryDto();
        industryArticleSecondCategoryDto.setNumber(industryArticleSecondCategory.getNumber());
        industryArticleSecondCategoryDto.setArticleNumber(industryArticleSecondCategory.getArticleNumber());
        industryArticleSecondCategoryDto.setSecondCategoryNumber(industryArticleSecondCategory.getSecondCategoryNumber());
        return industryArticleSecondCategoryDto;
    }

    default IndustryArticleSecondCategoryDto createAnotherIndustryArticleSecondCategoryDto() {
        IndustryArticleSecondCategoryDto industryArticleSecondCategoryDto = new IndustryArticleSecondCategoryDto();
        industryArticleSecondCategoryDto.setNumber(anotherIndustryArticleSecondCategory.getNumber());
        industryArticleSecondCategoryDto.setArticleNumber(anotherIndustryArticleSecondCategory.getArticleNumber());
        industryArticleSecondCategoryDto.setSecondCategoryNumber(anotherIndustryArticleSecondCategory.getSecondCategoryNumber());
        return industryArticleSecondCategoryDto;
    }
}
