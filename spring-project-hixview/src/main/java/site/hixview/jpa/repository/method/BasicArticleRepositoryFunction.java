package site.hixview.jpa.repository.method;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BasicArticleRepositoryFunction<T> {
    /**
     * SELECT Article
     */
    List<T> findByDate(LocalDate date);

    List<T> findByDate(LocalDate startDate, LocalDate endDate);

    List<T> findTopByDate();

    Optional<T> findByNumber(Long number);

    Optional<T> findByName(String name);

    Optional<T> findByLink(String link);

    /**
     * REMOVE Article
     */
    void deleteByNumber(Long number);
}
