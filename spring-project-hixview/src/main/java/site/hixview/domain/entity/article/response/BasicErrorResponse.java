package site.hixview.domain.entity.article.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

import static site.hixview.domain.vo.Word.FIELD_ERROR_MAP;

public record BasicErrorResponse(
        @JsonProperty(FIELD_ERROR_MAP)
        Map<String, String> fieldErrorMap
) implements Serializable {
}
