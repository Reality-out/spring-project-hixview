package site.hixview.aggregate.enums;

import lombok.Getter;

@Getter
public enum Classification {
    COMPANY("기업"),
    INDUSTRY("산업"),
    ECONOMY("경제");

    private final String value;

    Classification(String value) {
        this.value = value;
    }
}
