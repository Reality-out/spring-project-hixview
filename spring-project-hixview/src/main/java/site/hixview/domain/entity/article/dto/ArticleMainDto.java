package site.hixview.domain.entity.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import site.hixview.domain.validation.annotation.ClassificationConstraint;

@Getter
@Setter
public class ArticleMainDto {

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 80)
    private String imagePath;

    @NotBlank
    @Size(max = 36)
    private String summary;

    @ClassificationConstraint
    private String classification;
}
