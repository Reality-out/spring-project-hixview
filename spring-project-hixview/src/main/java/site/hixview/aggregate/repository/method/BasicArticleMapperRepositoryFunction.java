package site.hixview.aggregate.repository.method;

import java.util.List;

public interface BasicArticleMapperRepositoryFunction<T> extends BasicMapperRepositoryFunction<T> {
    /**
     * SELECT Mapper
     */
    List<T> getMappersByArticleNumber(Long articleNumber);
}
