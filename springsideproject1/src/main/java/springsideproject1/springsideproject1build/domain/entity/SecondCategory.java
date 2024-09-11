package springsideproject1.springsideproject1build.domain.entity;

import lombok.Getter;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_SECOND_CATEGORY_WITH_THAT_KOREAN_VALUE;

@Getter
public enum SecondCategory {
    BANK("은행"),
    DISPLAY_EQUIPMENT("디스플레이 장비"),
    DISPLAY_PANEL("디스플레이 패널"),
    ELECTRIC_VEHICLE_MANUFACTURING("전기차 제조"),
    GENERAL_CONSTRUCTION("종합건설"),
    INTERACTIVE_MEDIA_AND_SERVICE("양방향 미디어와 서비스"),
    SECONDARY_BATTERY_MANUFACTURING("2차전지 제조"),
    SEMICONDUCTOR_MANUFACTURING("반도체 제조"),
    SPECIALTY_CONSTRUCTION("전문건설"),
    STOCK("증권");

    private final String secondCategoryValue;

    SecondCategory(String secondCategoryValue) {
        this.secondCategoryValue = secondCategoryValue;
    }

    public static boolean containedWithSecondCategory(String str) {
        for (SecondCategory enumValue : SecondCategory.values()) {
            if (enumValue.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containedWithSecondCategoryValue(String str) {
        for (SecondCategory enumValue : SecondCategory.values()) {
            if (enumValue.secondCategoryValue.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static SecondCategory convertToSecondCategory(String str) {
        for (SecondCategory enumValue : SecondCategory.values()) {
            if (enumValue.secondCategoryValue.equals(str)) {
                return enumValue;
            }
        }
        throw new NotFoundException(NO_SECOND_CATEGORY_WITH_THAT_KOREAN_VALUE);
    }
}
