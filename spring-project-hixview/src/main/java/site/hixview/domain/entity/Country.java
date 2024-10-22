package site.hixview.domain.entity;

import lombok.Getter;

@Getter
public enum Country {
    GLOBAL("글로벌"),
    AMERICA("미국"),
    CHINA("중국"),
    GERMANY("독일"),
    INDIA("인도"),
    SOUTH_KOREA("대한민국");

    private final String value;

    Country(String value) {
        this.value = value;
    }
}
