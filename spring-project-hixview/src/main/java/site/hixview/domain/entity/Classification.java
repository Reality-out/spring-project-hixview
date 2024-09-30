package site.hixview.domain.entity;

import lombok.Getter;

@Getter
public enum Classification {
    COMPANY("기업"),
    INDUSTRY("산업");

    private final String classificationValue;

    Classification(String classificationValue) {
        this.classificationValue = classificationValue;
    }

    public String getValue() {
        return classificationValue;
    }
}
