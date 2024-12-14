package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.PressEntity;
import site.hixview.support.spring.util.PressTestUtils;

public interface PressEntityTestUtils extends PressTestUtils {
    /**
     * Create
     */
    default PressEntity createPressEntity() {
        return new PressEntity(press.getKoreanName(), press.getEnglishName());
    }

    default PressEntity createAnotherPressEntity() {
        return new PressEntity(press.getKoreanName(), press.getEnglishName());
    }
}
