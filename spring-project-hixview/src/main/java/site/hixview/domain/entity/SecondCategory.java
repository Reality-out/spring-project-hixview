package site.hixview.domain.entity;

import lombok.Getter;

@Getter
public enum SecondCategory {
    BANK("은행"),
    CATHODE_MATERIAL_MANUFACTURING("양극재 제조"),
    CLIMATE_CRISIS("기후 위기"),
    DISPLAY_EQUIPMENT("디스플레이 장비"),
    DISPLAY_PANEL("디스플레이 패널"),
    ELECTRIC_VEHICLE_MANUFACTURING("전기차 제조"),
    FIFTH_GENERATION_SERVICE("5G 서비스"),
    GENERAL_CONSTRUCTION("종합건설"),
    INTERACTIVE_MEDIA_AND_SERVICE("양방향 미디어와 서비스"),
    MOBILE_COMMUNICATION_SERVICE("이동 통신 서비스"),
    SECONDARY_BATTERY_MANUFACTURING("2차전지 제조"),
    SEMICONDUCTOR_EQUIPMENT("반도체 장비"),
    SEMICONDUCTOR_MANUFACTURING("반도체 제조"),
    SIXTH_GENERATION_SERVICE("6G 서비스"),
    SPECIALTY_CONSTRUCTION("전문건설"),
    STOCK("증권"),
    TELECOMMUNICATION_EQUIPMENT("통신 장비");

    private final String value;

    SecondCategory(String value) {
        this.value = value;
    }
}
