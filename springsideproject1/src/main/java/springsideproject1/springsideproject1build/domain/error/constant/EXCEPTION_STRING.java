package springsideproject1.springsideproject1build.domain.error.constant;

public interface EXCEPTION_STRING {
    // Simple String
    String ERROR = "error";
    String ERROR_SINGLE = "errorSingle";
    String ERRORS_ARE = "errors = {}";

    // Not Relative with any Exception class
    String BEAN_VALIDATION_ERROR = "beanValidationError";

    // Relative with an Exception class
    String EXIST_COMPANY_ARTICLE_ERROR = "existCompanyArticleError";
    String INDEX_OUT_OF_BOUND_ERROR = "indexOutOfBoundError";
    String NOT_FOUND_COMPANY_ARTICLE_ERROR = "companyArticleNotFoundError";
    String NOT_FOUND_COMPANY_ERROR = "companyNotFoundError";
    String NUMBER_FORMAT_INTEGER_ERROR = "numberFormatIntegerError";
    String NUMBER_FORMAT_LOCAL_DATE_ERROR = "numberFormatLocalDateError";
}
