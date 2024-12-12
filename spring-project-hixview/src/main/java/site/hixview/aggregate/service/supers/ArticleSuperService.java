package site.hixview.aggregate.service.supers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ArticleSuperService<T> extends ServiceWithNumberIdentifier<T> {
    List<T> getByDate(LocalDate date);

    List<T> getByDateRange(LocalDate startDate, LocalDate endDate);

    List<T> getBySubjectCountry(String subjectCountry);

    List<T> getByImportance(String importance);

    Optional<T> getByName(String name);

    Optional<T> getByLink(String link);
}
