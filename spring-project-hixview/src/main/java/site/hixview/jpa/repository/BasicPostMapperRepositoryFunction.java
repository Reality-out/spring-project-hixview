package site.hixview.jpa.repository;

import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.Optional;

public interface BasicPostMapperRepositoryFunction<T> extends BasicMapperRepositoryFunction<T> {
    /**
     * SELECT Mapper
     */
    Optional<T> findByNumber(Long postNumber);
}
