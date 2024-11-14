package site.hixview.aggregate.repository;

import site.hixview.aggregate.domain.EconomyContent;

import java.util.List;
import java.util.Optional;

public interface EconomyContentRepository {
    /**
     * SELECT EconomyContent
     */
    List<EconomyContent> getEconomyContents();

    Optional<EconomyContent> getEconomyContentByNumber(Long number);

    Optional<EconomyContent> getEconomyContentByName(String name);

    /**
     * INSERT EconomyContent
     */
    void saveEconomyContent(EconomyContent economyContent);

    /**
     * UPDATE EconomyContent
     */
    void updateEconomyContent(EconomyContent economyContent);

    /**
     * REMOVE EconomyContent
     */
    void deleteEconomyContentByNumber(Long number);
}