package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.PressEntity;

import java.util.Optional;

@Repository
public interface PressEntityRepository extends JpaRepository<PressEntity, Long> {
    /**
     * SELECT Press
     */
    Optional<PressEntity> findByNumber(Long number);

    Optional<PressEntity> findByKoreanName(String koreanName);

    Optional<PressEntity> findByEnglishName(String englishName);

    /**
     * REMOVE Press
     */
    void deleteByNumber(Long number);

    /**
     * CHECK Press
     */
    boolean existsByNumber(Long number);
}
