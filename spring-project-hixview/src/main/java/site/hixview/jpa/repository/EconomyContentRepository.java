package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.jpa.entity.EconomyContentEntity;

import java.util.Optional;

public interface EconomyContentRepository extends JpaRepository<EconomyContentEntity, Long> {
    /**
     * SELECT EconomyContent
     */
    Optional<EconomyContent> findByNumber(Long number);

    Optional<EconomyContent> findByName(String name);

    /**
     * REMOVE EconomyContent
     */
    void deleteByNumber(Long number);
}