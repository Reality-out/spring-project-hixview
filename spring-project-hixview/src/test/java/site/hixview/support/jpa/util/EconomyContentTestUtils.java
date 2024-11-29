package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.EconomyContentEntity;

public interface EconomyContentTestUtils {
    /**
     * Create
     */
    default EconomyContentEntity createEconomyContent() {
        return new EconomyContentEntity("합병");
    }

    default EconomyContentEntity createAnotherEconomyContent() {
        return new EconomyContentEntity("대주주");
    }
}
