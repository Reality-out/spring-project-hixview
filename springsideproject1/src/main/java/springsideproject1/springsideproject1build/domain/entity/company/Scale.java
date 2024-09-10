package springsideproject1.springsideproject1build.domain.entity.company;

import lombok.Getter;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_SCALE_WITH_THAT_KOREAN_VALUE;

@Getter
public enum Scale {
    BIG("대기업"),
    MIDDLE("중견기업"),
    SMALL("중소기업");

    private final String scaleValue;

    Scale(String scaleValue) {
        this.scaleValue = scaleValue;
    }

    public static boolean containsWithScale(String str) {
        for (Scale enumValue : Scale.values()) {
            if (enumValue.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWithScaleValue(String str) {
        for (Scale enumValue : Scale.values()) {
            if (enumValue.scaleValue.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static Scale convertToScale(String str) {
        for (Scale enumValue : Scale.values()) {
            if (enumValue.scaleValue.equals(str)) {
                return enumValue;
            }
        }
        throw new NotFoundException(NO_SCALE_WITH_THAT_KOREAN_VALUE);
    }
}
