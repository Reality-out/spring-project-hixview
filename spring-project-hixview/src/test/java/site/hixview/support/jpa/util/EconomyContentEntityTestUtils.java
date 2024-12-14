package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.support.spring.util.EconomyContentTestUtils;

public interface EconomyContentEntityTestUtils extends EconomyContentTestUtils {
    /**
     * Create
     */
    default EconomyContentEntity createEconomyContentEntity() {
        return new EconomyContentEntity(economyContent.getName());
    }

    default EconomyContentEntity createAnotherEconomyContentEntity() {
        return new EconomyContentEntity(anotherEconomyContent.getName());
    }
}
