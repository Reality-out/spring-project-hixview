package site.hixview.jpa.repository;

import site.hixview.aggregate.domain.EconomyContent;

import java.util.Optional;

public interface EconomyContentRepository {
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