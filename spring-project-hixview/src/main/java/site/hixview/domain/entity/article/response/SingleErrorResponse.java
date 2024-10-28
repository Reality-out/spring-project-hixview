package site.hixview.domain.entity.article.response;

import java.io.Serializable;
import java.util.Map;

public record SingleErrorResponse(String layoutPath, Map<String, String> fieldErrorMap) implements Serializable {
}
