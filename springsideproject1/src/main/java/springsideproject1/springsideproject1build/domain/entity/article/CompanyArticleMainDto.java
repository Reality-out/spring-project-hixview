package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyArticleMainDto {

    @NotBlank
    private String name;

    @NotBlank
    private String imagePath;

    @NotBlank
    private String summary;
}
