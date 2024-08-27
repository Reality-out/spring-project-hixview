package springsideproject1.springsideproject1build.domain.entity.company;

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
    SECONDARY_BATTERY("2차전지"),
    SEMICONDUCTOR("반도체"),
    SPACE("우주");

    private final String firstCategoryValue;

    FirstCategory(String firstCategoryValue) {
        this.firstCategoryValue = firstCategoryValue;
    }

    public static boolean containsWithFirstCategory(String str) {
        for (FirstCategory enumValue : FirstCategory.values()) {
            if (enumValue.name().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
