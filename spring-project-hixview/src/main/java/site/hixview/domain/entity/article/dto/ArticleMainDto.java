package site.hixview.domain.entity.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import site.hixview.domain.validation.annotation.ClassificationConstraint;

@Getter
@Setter
public class ArticleMainDto {

    @NotBlank(message = "{NotBlank.article.name}")
    private String name;

    @NotBlank(message = "{NotBlank.article.imagePath}")
    @Size(max = 80, message = "{Size.article.imagePath}")
    private String imagePath;

    @NotBlank(message = "{NotBlank.article.summary}")
    @Size(max = 36, message = "{Size.article.summary}")
    private String summary;

    @ClassificationConstraint
    private String classification;
}
