package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.jpa.entity.PressEntity;

import java.util.Optional;

@Repository
public interface PressRepository extends JpaRepository<PressEntity, Long> {
    /**
     * SELECT PressEntity
     */
    Optional<PressEntity> findByNumber(Long number);

    Optional<PressEntity> findByKoreanName(String koreanName);

    Optional<PressEntity> findByEnglishName(String englishName);

    /**
     * REMOVE PressEntity
     */
    @Transactional
    void deleteByNumber(Long number);
}
