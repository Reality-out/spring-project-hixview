package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.jpa.entity.EconomyContentEntity;

import java.util.Optional;

@Repository
public interface EconomyContentRepository extends JpaRepository<EconomyContentEntity, Long> {
    /**
     * SELECT EconomyContent
     */
    Optional<EconomyContent> findByNumber(Long number);

    Optional<EconomyContent> findByName(String name);

    /**
     * REMOVE EconomyContent
     */
    @Transactional
    void deleteByNumber(Long number);
}