package springsideproject1.springsideproject1build.domain.company;

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
}
