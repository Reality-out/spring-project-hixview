package springsideproject1.springsideproject1build.domain.entity.company;

import lombok.Getter;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_FIRST_CATEGORY_WITH_THAT_KOREAN_VALUE;

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

    public static boolean containsWithFirstCategoryValue(String str) {
        for (FirstCategory enumValue : FirstCategory.values()) {
            if (enumValue.firstCategoryValue.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static FirstCategory convertToFirstCategory(String str) {
        for (FirstCategory enumValue : FirstCategory.values()) {
            if (enumValue.firstCategoryValue.equals(str)) {
                return enumValue;
            }
        }
        throw new NotFoundException(NO_FIRST_CATEGORY_WITH_THAT_KOREAN_VALUE);
    }
}
