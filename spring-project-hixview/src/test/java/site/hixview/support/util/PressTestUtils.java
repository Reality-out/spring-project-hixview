package site.hixview.support.util;

import site.hixview.jpa.entity.PressEntity;

public interface PressTestUtils {
    /**
     * Create
     */
    default PressEntity createTestPress() {
        return new PressEntity("아주경제", "AJU_ECONOMY");
    }
}
