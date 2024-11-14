package site.hixview.aggregate.repository.method;

import java.util.List;
import java.util.Optional;

public interface BasicMapperRepositoryFunction<T> {
    /**
     * SELECT Mapper
     */
    List<T> getMappers();
    
    Optional<T> getMapperByNumber(Long number);
    
    /**
     * INSERT Mapper
     */
    Long saveMapper(T mapper);

    /**
     * UPDATE Mapper
     */
    void updateMapper(T mapper);

    /**
     * REMOVE Mapper
     */
    void deleteMapperByNumber(Long number);
}
