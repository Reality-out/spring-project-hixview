package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.PressEntity;

public interface PressEntityTestUtils {
    /**
     * Create
     */
    default PressEntity createPressEntity() {
        return new PressEntity("아주경제", "AJU_ECONOMY");
    }

    default PressEntity createAnotherPressEntity() {
        return new PressEntity("아시아경제", "ASIA_ECONOMY");
    }
}
