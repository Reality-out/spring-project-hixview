package springsideproject1.springsideproject1build.domain.entity.company;

import lombok.Getter;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_COUNTRY_WITH_THAT_VALUE;

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

    public static boolean containsWithCountryValue(String str) {
        for (Country enumValue : Country.values()) {
            if (enumValue.countryValue.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static Country convertToCountry(String str) {
        for (Country enumValue : Country.values()) {
            if (enumValue.countryValue.equals(str)) {
                return enumValue;
            }
        }
        throw new NotFoundException(NO_COUNTRY_WITH_THAT_VALUE);
    }
}
