package site.hixview.jpa.repository.method;

import java.util.Optional;

public interface BasicMapperRepositoryFunction<T> {
    /**
     * SELECT Mapper
     */
    Optional<T> findByNumber(Long number);
    
    /**
     * REMOVE Mapper
     */
    void deleteByNumber(Long number);
}