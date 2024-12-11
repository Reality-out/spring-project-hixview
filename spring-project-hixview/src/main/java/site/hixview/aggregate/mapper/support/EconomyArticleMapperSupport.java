package site.hixview.aggregate.mapper.support;

import org.mapstruct.Named;

import java.util.List;

import static site.hixview.aggregate.util.JsonUtils.mapLongList;
import static site.hixview.aggregate.util.JsonUtils.parseToLongList;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE;

public interface EconomyArticleMapperSupport extends ArticleMapperSupport {
    @Named("mappedEconomyContentNumbersToDomain")
    default List<Long> mappedEconomyContentNumbersToDomain(String mappedEconomyContentNumbers) {
        return parseToLongList(mappedEconomyContentNumbers, MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE);
    }

    @Named("mappedEconomyContentNumbersToDto")
    default String mappedEconomyContentNumbersToDto(List<Long> mappedEconomyContentNumbers) {
        return mapLongList(mappedEconomyContentNumbers, MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE);
    }
}
