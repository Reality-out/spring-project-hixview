package site.hixview.aggregate.repository;

import site.hixview.aggregate.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

public interface BasicPostMapperRepositoryFunction<T> extends BasicMapperRepositoryFunction<T> {
    /**
     * SELECT Mapper
     */
    List<T> getMappersByPostNumber(Long postNumber);
}
