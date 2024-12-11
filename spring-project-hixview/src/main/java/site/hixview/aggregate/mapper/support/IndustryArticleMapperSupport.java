package site.hixview.aggregate.mapper.support;

import org.mapstruct.Named;

import java.util.List;

import static site.hixview.aggregate.util.JsonUtils.mapLongList;
import static site.hixview.aggregate.util.JsonUtils.parseToLongList;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE;

public interface IndustryArticleMapperSupport extends ArticleMapperSupport {
    @Named("mappedSecondCategoryNumbersToDomain")
    default List<Long> mappedSecondCategoryNumbersToDomain(String mappedSecondCategoryNumbers) {
        return parseToLongList(mappedSecondCategoryNumbers, MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE);
    }

    @Named("mappedSecondCategoryNumbersToDto")
    default String mappedSecondCategoryNumbersToDto(List<Long> mappedSecondCategoryNumbers) {
        return mapLongList(mappedSecondCategoryNumbers, MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE);
    }
}
