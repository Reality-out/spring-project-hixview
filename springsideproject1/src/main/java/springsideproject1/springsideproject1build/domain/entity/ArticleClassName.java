package springsideproject1.springsideproject1build.domain.entity;

import lombok.Getter;

@Getter
public enum ArticleClassName {
    COMPANY_ARTICLE("기업 기사"),
    INDUSTRY_ARTICLE("산업 기사");

    private final String articleClassNameValue;

    ArticleClassName(String articleClassNameValue) {
        this.articleClassNameValue = articleClassNameValue;
    }

    public String getValue() {
        return articleClassNameValue;
    }
}
