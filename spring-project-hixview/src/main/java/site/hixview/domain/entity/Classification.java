package site.hixview.domain.entity;

import lombok.Getter;

@Getter
public enum Classification {
    COMPANY("기업"),
    INDUSTRY("산업");

    private final String value;

    Classification(String value) {
        this.value = value;
    }
}
