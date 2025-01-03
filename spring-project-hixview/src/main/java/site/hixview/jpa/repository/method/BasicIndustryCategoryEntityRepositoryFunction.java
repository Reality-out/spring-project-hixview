package site.hixview.jpa.repository.method;

import java.util.Optional;

public interface BasicIndustryCategoryEntityRepositoryFunction<T> {
    /**
     * SELECT Category
     */
    Optional<T> findByNumber(Long number);

    Optional<T> findByKoreanName(String koreanName);

    Optional<T> findByEnglishName(String englishName);

    /**
     * REMOVE Category
     */
    void deleteByNumber(Long number);

    /**
     * CHECK Category
     */
    boolean existsByNumber(Long number);
}
