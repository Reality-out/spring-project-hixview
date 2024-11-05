package site.hixview.domain.entity.article.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

import static site.hixview.domain.vo.Word.FIELD_ERROR_MAP;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;

public record BeanValidationErrorResponse(
        @JsonProperty(IS_BEAN_VALIDATION_ERROR)
        Boolean isBeanValidationError,

        @JsonProperty(FIELD_ERROR_MAP)
        Map<String, String> fieldErrorMap
) implements Serializable {
}
