package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import springsideproject1.springsideproject1build.domain.validation.annotation.ArticleClassName;

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

    @ArticleClassName
    private String articleClassName;
}
