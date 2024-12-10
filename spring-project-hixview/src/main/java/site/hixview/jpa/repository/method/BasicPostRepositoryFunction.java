package site.hixview.jpa.repository.method;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BasicPostRepositoryFunction<T> {
    /**
     * SELECT Post
     */
    List<T> findByDate(LocalDate date);

    List<T> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<T> findByNumber(Long number);

    Optional<T> findByName(String name);

    Optional<T> findByLink(String link);

    /**
     * REMOVE Post
     */
    void deleteByNumber(Long number);

    /**
     * CHECK Post
     */
    boolean existsByNumber(Long number);
}
