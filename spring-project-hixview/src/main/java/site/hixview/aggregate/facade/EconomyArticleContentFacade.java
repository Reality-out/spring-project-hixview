package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.domain.EconomyArticleContent.EconomyArticleContentBuilder;
import site.hixview.aggregate.dto.EconomyArticleContentDto;

public class EconomyArticleContentFacade {
    public EconomyArticleContentBuilder createBuilder(EconomyArticleContent mapper) {
        return EconomyArticleContent.builder()
                .number(mapper.getNumber())
                .articleNumber(mapper.getArticleNumber())
                .contentNumber(mapper.getContentNumber());
    }

    public EconomyArticleContentBuilder createBuilder(EconomyArticleContentDto mapperDto) {
        return EconomyArticleContent.builder()
                .number(mapperDto.getNumber())
                .articleNumber(mapperDto.getArticleNumber())
                .contentNumber(mapperDto.getContentNumber());
    }
}
