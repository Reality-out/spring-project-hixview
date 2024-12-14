package site.hixview.aggregate.mapper.support;

import org.mapstruct.Named;

import java.util.List;

import static site.hixview.aggregate.util.JsonUtils.mapLongList;
import static site.hixview.aggregate.util.JsonUtils.parseToLongList;
import static site.hixview.aggregate.vo.WordSnake.MAPPED_ARTICLE_NUMBERS_SNAKE;

public interface BlogPostMapperSupport {
    @Named("mappedArticleNumbersToDomain")
    default List<Long> mappedArticleNumbersToDomain(String mappedArticleNumbers) {
        return parseToLongList(mappedArticleNumbers, MAPPED_ARTICLE_NUMBERS_SNAKE);
    }

    @Named("mappedArticleNumbersToDto")
    default String mappedArticleNumbersToDto(List<Long> mappedArticleNumbers) {
        return mapLongList(mappedArticleNumbers, MAPPED_ARTICLE_NUMBERS_SNAKE);
    }
}
