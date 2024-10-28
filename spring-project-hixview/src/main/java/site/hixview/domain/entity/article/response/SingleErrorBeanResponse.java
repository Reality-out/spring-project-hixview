package site.hixview.domain.entity.article.response;

import java.io.Serializable;
import java.util.Map;

public record SingleErrorBeanResponse(String layoutPath, Boolean isBeanValidationError, Map<String, String> fieldErrorMap) implements Serializable {
}
