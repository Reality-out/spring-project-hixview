package site.hixview.domain.entity.article.response;

import java.io.Serializable;

public record SingleArticleSuccessResponse(String name, String redirectedURL) implements Serializable {
}
