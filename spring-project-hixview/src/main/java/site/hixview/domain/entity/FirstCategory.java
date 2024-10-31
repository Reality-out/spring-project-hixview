package site.hixview.domain.entity;

import lombok.Getter;

@Getter
public enum FirstCategory {
    CONSTRUCTION("건설"),
    DEFENSE("방산"),
    DISPLAY("디스플레이"),
    ELECTRIC_VEHICLE("전기차"),
    ENERGY("에너지"),
    ENVIRONMENT("환경"),
    FINANCE("금융"),
    IT_SERVICE("IT 서비스"),
    MACHINE("기계"),
    SECONDARY_BATTERY("2차전지"),
    SEMICONDUCTOR("반도체"),
    SPACE("우주"),
    TELECOMMUNICATION("통신");

    private final String value;

    FirstCategory(String value) {
        this.value = value;
    }
}
