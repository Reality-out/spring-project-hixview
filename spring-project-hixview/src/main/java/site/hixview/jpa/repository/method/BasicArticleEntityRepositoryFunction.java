package site.hixview.jpa.repository.method;

import site.hixview.jpa.entity.PressEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BasicArticleEntityRepositoryFunction<T> {
    /**
     * SELECT Article
     */
    List<T> findByDate(LocalDate date);

    List<T> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<T> findBySubjectCountry(String subjectCountry);

    List<T> findByImportance(String importance);

    List<T> findByPress(PressEntity press);

    Optional<T> findByNumber(Long number);

    Optional<T> findByName(String name);

    Optional<T> findByLink(String link);

    /**
     * REMOVE Article
     */
    void deleteByNumber(Long number);

    /**
     * CHECK Article
     */
    boolean existsByNumber(Long number);
}
