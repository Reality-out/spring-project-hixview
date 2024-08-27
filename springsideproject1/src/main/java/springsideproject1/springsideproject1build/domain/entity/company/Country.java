package springsideproject1.springsideproject1build.domain.entity.company;

import lombok.Getter;

@Getter
public enum Country {
    SOUTH_KOREA("대한민국"),
    AMERICA("미국"),
    CHINA("중국"),
    INDIA("인도");

    private final String countryValue;

    Country(String countryValue) {
        this.countryValue = countryValue;
    }

    public static boolean containsWithCountry(String str) {
        for (Country enumValue : Country.values()) {
            if (enumValue.name().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
