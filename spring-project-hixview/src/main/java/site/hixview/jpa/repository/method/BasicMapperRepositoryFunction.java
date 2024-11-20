package site.hixview.jpa.repository.method;

import java.util.Optional;

public interface BasicMapperRepositoryFunction<T, S, U> {
    /**
     * SELECT Mapper
     */
    Optional<T> findByNumber(Long number);

    /**
     * REMOVE Mapper
     */
    void deleteByNumber(Long number);
}
