package site.hixview.domain.entity.article.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.Word.REDIRECT_PATH;

public record BasicSuccessResponse(
        @JsonProperty(NAME)
        String name,

        @JsonProperty(REDIRECT_PATH)
        String redirectPath
) implements Serializable {
}
