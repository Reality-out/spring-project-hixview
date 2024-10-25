package site.hixview.domain.entity.article.response;

import java.io.Serializable;

public record SingleSuccessResponse(String name, String redirectPath) implements Serializable {
}
