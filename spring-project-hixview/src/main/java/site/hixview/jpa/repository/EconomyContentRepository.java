package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.jpa.entity.EconomyContentEntity;

import java.util.Optional;

@Repository
public interface EconomyContentRepository extends JpaRepository<EconomyContentEntity, Long> {
    /**
     * SELECT EconomyContent
     */
    Optional<EconomyContentEntity> findByNumber(Long number);

    Optional<EconomyContentEntity> findByName(String name);

    /**
     * REMOVE EconomyContent
     */
    @Transactional
    void deleteByNumber(Long number);
}