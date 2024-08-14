package springsideproject1.springsideproject1build.domain.company;

import lombok.Getter;

@Getter
public enum SecondCategory {
    BANK("은행"),
    DISPLAY_EQUIPMENT("디스플레이 장비"),
    DISPLAY_PANEL("디스플레이 패널"),
    ELECTRIC_VEHICLE_MANUFACTURING("전기차 제조"),
    GENERAL_CONSTRUCTION("종합건설"),
    GENERAL_IT_SERVICE("종합 IT 서비스"),
    SECONDARY_BATTERY_MANUFACTURING("2차전지 제조"),
    SEMICONDUCTOR_MANUFACTURING("반도체 제조"),
    STOCK("증권");

    private final String secondCategoryValue;

    SecondCategory(String secondCategoryValue) {
        this.secondCategoryValue = secondCategoryValue;
    }
}
