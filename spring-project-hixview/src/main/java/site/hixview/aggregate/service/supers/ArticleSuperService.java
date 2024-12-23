package site.hixview.aggregate.service.supers;

import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ArticleSuperService<T> extends CrudAllowedServiceWithNumberId<T> {
    List<T> getByDate(LocalDate date);

    List<T> getByDateRange(LocalDate startDate, LocalDate endDate);

    List<T> getBySubjectCountry(Country subjectCountry);

    List<T> getByImportance(Importance importance);

    List<T> getByPress(Press press);

    Optional<T> getByName(String name);

    Optional<T> getByLink(String link);
}
