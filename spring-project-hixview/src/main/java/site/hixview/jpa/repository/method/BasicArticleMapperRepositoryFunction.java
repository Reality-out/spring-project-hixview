package site.hixview.jpa.repository.method;

import java.util.Optional;

public interface BasicArticleMapperRepositoryFunction<T> extends BasicMapperRepositoryFunction<T> {
    /**
     * SELECT Mapper
     */
    Optional<T> findByNumber(Long number);
}
