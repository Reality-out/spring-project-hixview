package springsideproject1.springsideproject1build.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionString {
    // Not Relative with any Exception class
    public static final String BEAN_VALIDATION_ERROR = "beanValidationError";
    public static final String IS_BEAN_VALIDATION_ERROR = "isBeanValidationError";

    // Relative with an Exception class
    public static final String INDEX_OUT_OF_BOUND_ERROR = "indexOutOfBoundError";
    public static final String NOT_BLANK_ARTICLE_ERROR = "notBlankArticleError";
    public static final String NOT_EXIST_COMPANY_ERROR = "notExistCompanyError";
    public static final String NOT_FOUND_COMPANY_ARTICLE_ERROR = "notFoundCompanyArticleError";
    public static final String NOT_FOUND_INDUSTRY_ARTICLE_ERROR = "notFoundIndustryArticleError";
    public static final String NOT_FOUND_ARTICLE_MAIN_ERROR = "notFoundArticleMainError";
    public static final String NOT_FOUND_COMPANY_ERROR = "notFoundCompanyError";
    public static final String NOT_FOUND_FIRST_CATEGORY_ERROR = "notFoundFirstCategoryError";
    public static final String NOT_FOUND_SECOND_CATEGORY_ERROR = "notFoundSecondCategoryError";
    public static final String NUMBER_FORMAT_INTEGER_ERROR = "numberFormatIntegerError";
    public static final String NUMBER_FORMAT_LOCAL_DATE_ERROR = "numberFormatLocalDateError";
}
