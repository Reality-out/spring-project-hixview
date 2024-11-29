package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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
    void deleteByNumber(Long number);

    /**
     * CHECK EconomyContent
     */
    boolean existsByNumber(Long number);
}