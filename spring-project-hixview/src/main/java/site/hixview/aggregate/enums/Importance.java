package site.hixview.aggregate.enums;

import lombok.Getter;

@Getter
public enum Importance {
    MODERATE("보통"),
    IMPORTANT("중요"),
    VERY_IMPORTANT("매우 중요");

    private final String value;

    Importance(String value) {
        this.value = value;
    }
}
