package springsideproject1.springsideproject1build.domain.entity;

import lombok.Getter;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_ARTICLE_CLASS_NAME_WITH_THAT_VALUE;

@Getter
public enum ArticleClassName {
    COMPANY_ARTICLE("기업 기사"),
    INDUSTRY_ARTICLE("산업 기사");

    private final String articleClassNameValue;

    ArticleClassName(String articleClassNameValue) {
        this.articleClassNameValue = articleClassNameValue;
    }

    public static boolean containedWithArticleClassName(String str) {
        for (ArticleClassName enumValue : ArticleClassName.values()) {
            if (enumValue.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containedWithArticleClassNameValue(String str) {
        for (ArticleClassName enumValue : ArticleClassName.values()) {
            if (enumValue.articleClassNameValue.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static ArticleClassName convertToArticleClassName(String str) {
        for (ArticleClassName enumValue : ArticleClassName.values()) {
            if (enumValue.articleClassNameValue.equals(str)) {
                return enumValue;
            }
        }
        throw new NotFoundException(NO_ARTICLE_CLASS_NAME_WITH_THAT_VALUE);
    }
}
