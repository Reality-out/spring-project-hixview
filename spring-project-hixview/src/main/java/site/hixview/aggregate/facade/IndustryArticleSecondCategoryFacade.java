package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory.IndustryArticleSecondCategoryBuilder;
import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;

public class IndustryArticleSecondCategoryFacade {
    public IndustryArticleSecondCategoryBuilder createBuilder(IndustryArticleSecondCategory mapper) {
        return IndustryArticleSecondCategory.builder()
                .number(mapper.getNumber())
                .articleNumber(mapper.getArticleNumber())
                .secondCategoryNumber(mapper.getSecondCategoryNumber());
    }

    public IndustryArticleSecondCategoryBuilder createBuilder(IndustryArticleSecondCategoryDto mapperDto) {
        return IndustryArticleSecondCategory.builder()
                .number(mapperDto.getNumber())
                .articleNumber(mapperDto.getArticleNumber())
                .secondCategoryNumber(mapperDto.getSecondCategoryNumber());
    }
}
