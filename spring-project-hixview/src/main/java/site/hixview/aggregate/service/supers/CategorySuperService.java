package site.hixview.aggregate.service.supers;

import java.util.Optional;

public interface CategorySuperService<T> extends ServiceWithNumberIdentifier<T> {
    Optional<T> getByKoreanName(String koreanName);

    Optional<T> getByEnglishName(String englishName);
}
