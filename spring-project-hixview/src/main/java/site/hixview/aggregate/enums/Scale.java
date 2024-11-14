package site.hixview.aggregate.enums;

import lombok.Getter;

@Getter
public enum Scale {
    BIG("대기업"),
    MIDDLE("중견기업"),
    SMALL("중소기업");

    private final String value;

    Scale(String value) {
        this.value = value;
    }
}
