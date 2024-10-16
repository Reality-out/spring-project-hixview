package site.hixview.domain.entity.article;

import java.io.Serializable;

public record SingleArticleSuccessResponse(String name, String redirectedURL) implements Serializable {
}
