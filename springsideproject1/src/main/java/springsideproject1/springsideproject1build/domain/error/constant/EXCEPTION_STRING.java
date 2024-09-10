package springsideproject1.springsideproject1build.domain.error.constant;

public interface EXCEPTION_STRING {
    // Simple String
    String ERROR = "error";
    String ERROR_SINGLE = "errorSingle";
    String ERRORS_ARE = "errors = {}";

    // Not Relative with any Exception class
    String BEAN_VALIDATION_ERROR = "beanValidationError";
    String IS_BEAN_VALIDATION_ERROR = "isBeanValidationError";

    // Relative with an Exception class
    String EXIST_COMPANY_ARTICLE_ERROR = "existCompanyArticleError";
    String INDEX_OUT_OF_BOUND_ERROR = "indexOutOfBoundError";
    String NOT_BLANK_ARTICLE_ERROR = "notBlankArticleError";
    String NOT_EXIST_COMPANY_ERROR = "NotFoundCompanyError";
    String NOT_FOUND_COMPANY_ARTICLE_ERROR = "notFoundCompanyArticleError";
    String NOT_FOUND_COMPANY_ARTICLE_MAIN_ERROR = "notFoundArticleMainError";
    String NOT_FOUND_INDUSTRY_ARTICLE_ERROR = "notFoundIndustryArticleError";
    String NOT_FOUND_INDUSTRY_ARTICLE_MAIN_ERROR = "notFoundIndustryArticleMainError";
    String NOT_FOUND_COMPANY_ERROR = "notFoundCompanyError";
    String NOT_FOUND_FIRST_CATEGORY_ERROR = "notFoundFirstCategoryError";
    String NOT_FOUND_SECOND_CATEGORY_ERROR = "notFoundSecondCategoryError";
    String NUMBER_FORMAT_INTEGER_ERROR = "numberFormatIntegerError";
    String NUMBER_FORMAT_LOCAL_DATE_ERROR = "numberFormatLocalDateError";
}
