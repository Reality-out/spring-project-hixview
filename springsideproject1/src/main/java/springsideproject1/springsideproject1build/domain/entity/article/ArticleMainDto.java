package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import springsideproject1.springsideproject1build.domain.validation.annotation.ArticleClassName;

@Getter
@Setter
public class ArticleMainDto {

    @NotBlank
    private String name;

    @NotBlank
    private String imagePath;

    @NotBlank
    private String summary;

    @ArticleClassName
    private String articleClassName;
}
