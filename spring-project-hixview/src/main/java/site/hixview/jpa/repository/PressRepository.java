package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.PressEntity;

import java.util.Optional;

@Repository
public interface PressRepository extends JpaRepository<PressEntity, Long> {
    /**
     * SELECT Press
     */
    Optional<Press> findByNumber(Long number);

    Optional<Press> findByKoreanName(String koreanName);

    Optional<Press> findByEnglishName(String englishName);

    /**
     * REMOVE Press
     */
    @Transactional
    void deleteByNumber(Long number);
}
