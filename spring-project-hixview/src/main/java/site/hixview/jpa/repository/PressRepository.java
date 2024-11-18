package site.hixview.jpa.repository;

import site.hixview.aggregate.domain.Press;

import java.util.Optional;

public interface PressRepository {
    /**
     * SELECT Press
     */
    Optional<Press> findByNumber(Long number);

    Optional<Press> findByKoreanName(String koreanName);

    Optional<Press> findByEnglishName(String englishName);

    /**
     * REMOVE Press
     */
    void deleteByNumber(Long number);
}
