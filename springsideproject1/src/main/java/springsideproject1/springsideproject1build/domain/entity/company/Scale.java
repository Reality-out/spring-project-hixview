package springsideproject1.springsideproject1build.domain.entity.company;

import lombok.Getter;

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
}
