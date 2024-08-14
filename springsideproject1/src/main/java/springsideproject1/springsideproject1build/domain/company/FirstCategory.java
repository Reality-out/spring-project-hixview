package springsideproject1.springsideproject1build.domain.company;

import lombok.Getter;

@Getter
public enum FirstCategory {
    CONSTRUCTION("건설"),
    DEFENSE("방산"),
    DISPLAY("디스플레이"),
    ELECTRONIC_CAR("전기차"),
    ENERGY("에너지"),
    ENVIRONMENT("환경"),
    FINANCE("금융"),
    IT("IT"),
    SECONDARY_BATTERY("2차 전지"),
    SEMICONDUCTOR("반도체"),
    SPACE("우주");

    private final String firstCategoryValue;

    FirstCategory(String firstCategoryValue) {
        this.firstCategoryValue = firstCategoryValue;
    }
}
