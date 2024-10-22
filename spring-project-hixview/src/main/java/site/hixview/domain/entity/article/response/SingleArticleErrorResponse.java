package site.hixview.domain.entity.article.response;

import java.io.Serializable;
import java.util.Map;

public record SingleArticleErrorResponse(String layoutPath, Boolean isBeanValidationError, Map<String, String> fieldErrorMap) implements Serializable {
}
