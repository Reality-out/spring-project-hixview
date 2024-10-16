package site.hixview.domain.entity.article;

import java.io.Serializable;
import java.util.Map;

public record SingleArticleErrorResponse(String layoutPath, Boolean isBeanValidationError, Map<String, String> fieldErrorMap) implements Serializable {
}
