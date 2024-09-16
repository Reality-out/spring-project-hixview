package site.hixview.domain.entity;

import lombok.Getter;

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

    public String getValue() {
        return secondCategoryValue;
    }
}
