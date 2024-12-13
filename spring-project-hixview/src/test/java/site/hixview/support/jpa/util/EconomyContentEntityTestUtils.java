package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyContentEntity;

public interface EconomyContentEntityTestUtils {
    /**
     * Create
     */
    default EconomyContentEntity createEconomyContentEntity() {
        return new EconomyContentEntity("합병");
    }

    default EconomyContentEntity createAnotherEconomyContentEntity() {
        return new EconomyContentEntity("대주주");
    }
}
